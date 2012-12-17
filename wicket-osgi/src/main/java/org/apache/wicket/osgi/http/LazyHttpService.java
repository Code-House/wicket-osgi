package org.apache.wicket.osgi.http;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class LazyHttpService implements HttpService {

    private HttpService delegate;
    private Map<String, WicketServletDefinition> waitingRegistrations = new HashMap<String, WicketServletDefinition>();
    private Map<String, WicketServletDefinition> registrations = new HashMap<String, WicketServletDefinition>();
    private Logger logger = LoggerFactory.getLogger(LazyHttpService.class);

    public LazyHttpService() {
    }

    public void registerServlet(String alias, Servlet servlet, Dictionary initparams, HttpContext context)
        throws ServletException, NamespaceException {
        if (delegate != null) {
            registrations.put(alias, new WicketServletDefinition(servlet, initparams, context));
            delegate.registerServlet(alias, servlet, initparams, context);
        } else {
            waitingRegistrations.put(alias, new WicketServletDefinition(servlet, initparams, context));
        }
    }

    public void registerResources(String alias, String name, HttpContext context) throws NamespaceException {
        // ignore
    }

    public void unregister(String alias) {
        if (delegate != null) {
            registrations.remove(alias);
            delegate.unregister(alias);
        } else {
            waitingRegistrations.remove(alias);
        }
    }

    public HttpContext createDefaultHttpContext() {
        if (delegate != null) {
            return delegate.createDefaultHttpContext();
        }
        throw new IllegalStateException("Can not create http context when delegate is unknown");
    }

    public void setHttpService(HttpService delegate) {
        this.delegate = delegate;
        Iterator<String> paths = waitingRegistrations.keySet().iterator();
        while (paths.hasNext()) {
            String path = paths.next();
            try {
                WicketServletDefinition servletDefinition = waitingRegistrations.remove(path);
                    delegate.registerServlet(path, servletDefinition.getServlet(), servletDefinition.getParams(), 
                        delegate.createDefaultHttpContext());
                registrations.put(path, servletDefinition);
            } catch (ServletException e) {
                logger.error("Can not register servlet", e);
            } catch (NamespaceException e) {
                logger.error("Can not register servlet", e);
            }
        }
    }

    public void removeHttpService() {
        if (delegate != null) {
            Iterator<String> paths = registrations.keySet().iterator();
            while (paths.hasNext()) {
                String path = paths.next();
                delegate.unregister(path);
                waitingRegistrations.put(path, registrations.remove(path));
            }
            delegate = null;
        }
    }

}
