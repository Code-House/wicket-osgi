package org.apache.wicket.osgi;

import org.apache.wicket.application.CompoundClassResolver;
import org.apache.wicket.application.IClassResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ApplicationClassLoaderCustomizer implements ServiceTrackerCustomizer {

    private final CompoundClassResolver resolver;

    private final BundleContext bundleContext;

    public ApplicationClassLoaderCustomizer(CompoundClassResolver resolver, BundleContext bundleContext) {
        this.resolver = resolver;
        this.bundleContext = bundleContext;
    }

    public void removedService(ServiceReference reference, Object service) {
        resolver.remove((IClassResolver) service);
    }

    public void modifiedService(ServiceReference reference, Object service) {
    }

    public Object addingService(ServiceReference reference) {
        IClassResolver service = (IClassResolver) bundleContext.getService(reference);
        resolver.add(service);
        return service;
    }

}