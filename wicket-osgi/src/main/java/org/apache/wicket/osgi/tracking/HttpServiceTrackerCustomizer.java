package org.apache.wicket.osgi.tracking;

import org.apache.wicket.osgi.http.LazyHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class HttpServiceTrackerCustomizer implements ServiceTrackerCustomizer {

    private final LazyHttpService httpService;
    private final BundleContext context;

    public HttpServiceTrackerCustomizer(LazyHttpService httpService, BundleContext context) {
        this.httpService = httpService;
        this.context = context;
    }

    public Object addingService(ServiceReference reference) {
        httpService.setHttpService((HttpService) context.getService(reference));
        return reference;
    }

    public void modifiedService(ServiceReference reference, Object service) {
        removedService(reference, service);
        addingService(reference);
    }

    public void removedService(ServiceReference reference, Object service) {
        httpService.removeHttpService();
    }

}
