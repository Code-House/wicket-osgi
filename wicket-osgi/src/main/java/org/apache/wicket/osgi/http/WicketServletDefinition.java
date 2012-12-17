package org.apache.wicket.osgi.http;

import java.util.Dictionary;

import javax.servlet.Servlet;

import org.osgi.service.http.HttpContext;

@SuppressWarnings("rawtypes")
public final class WicketServletDefinition {

    private Servlet servlet;
    private final Dictionary params;
    private final HttpContext context;

    public WicketServletDefinition(Servlet servlet, Dictionary params, HttpContext context) {
        this.servlet = servlet;
        this.params = params;
        this.context = context;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public Dictionary getParams() {
        return params;
    }

    public HttpContext getContext() {
        return context;
    }

}
