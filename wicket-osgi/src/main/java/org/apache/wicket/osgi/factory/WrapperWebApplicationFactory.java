package org.apache.wicket.osgi.factory;

import java.lang.reflect.Field;

import org.apache.wicket.Application;
import org.apache.wicket.application.CompoundClassResolver;
import org.apache.wicket.osgi.classloader.OsgiClassResolver;
import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WrapperWebApplicationFactory implements IWebApplicationFactory {

    private Logger logger = LoggerFactory.getLogger(WrapperWebApplicationFactory.class);
    private final IWebApplicationFactory delegate;

    public WrapperWebApplicationFactory(IWebApplicationFactory delegate) {
        this.delegate = delegate;
    }

    public WebApplication createApplication(WicketFilter filter) {
        WebApplication webApplication = delegate.createApplication(filter);
        //dirtyClassLoaderHack(webApplication);
        return webApplication;
    }

    public void destroy(WicketFilter filter) {
        delegate.destroy(filter);
    }

    private void dirtyClassLoaderHack(WebApplication service) {
        try {
            Bundle bundle = FrameworkUtil.getBundle(service.getClass());
            Field field = Application.class.getDeclaredField("settingsAccessible");
            field.setAccessible(true);
            field.set(service, true);

            CompoundClassResolver resolver = new CompoundClassResolver();
            resolver.add(new OsgiClassResolver(bundle));
            // let application discover initializers using OSGi, that's awesome !
            service.getApplicationSettings().setClassResolver(resolver);
        } catch (Exception e) {
            logger.warn("Application classloader override did not pass. Discovery of initializers will not work", e);
        }
    }
}
