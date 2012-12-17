package org.apache.wicket.osgi;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.http.HttpServlet;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketServlet;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ApplicationTrakcerCustomizer implements ServiceTrackerCustomizer {

    private final BundleContext context;

    public ApplicationTrakcerCustomizer(BundleContext context) {
        this.context = context;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object addingService(ServiceReference reference) {
        Bundle bundle = reference.getBundle();

        WebApplication service = (WebApplication) context.getService(reference);
        if (service != null) {
            WicketServlet wicketServlet = new WicketOsgiServlet((String) reference.getProperty("mount-point"), service);

            Dictionary properties = new Hashtable();
            properties.put("alias", reference.getProperty("mount-point"));
            properties.put("servlet-name", reference.getProperty("application-name"));

            return bundle.getBundleContext().registerService(HttpServlet.class.getName(), wicketServlet, properties);
        }
        return null;
    }

    public void modifiedService(ServiceReference reference, Object service) {
        removedService(reference, service);
        service = addingService(reference);
    }

    public void removedService(ServiceReference reference, Object service) {
        ServiceRegistration registration = (ServiceRegistration) service;
        registration.unregister();
        context.ungetService(reference);
    }

}
