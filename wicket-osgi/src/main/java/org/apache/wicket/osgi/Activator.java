package org.apache.wicket.osgi;

import org.apache.wicket.protocol.http.WebApplication;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

    private ServiceTracker applicationTracker;
    private BundleTracker classResolverBundleTracker;
    private BundleTracker fieldValueBundleTracker;

    public void start(BundleContext context) throws Exception {
        applicationTracker = new ServiceTracker(context, WebApplication.class.getName(),
            new ApplicationTrakcerCustomizer(context));
        applicationTracker.open();
        classResolverBundleTracker = new BundleTracker(context, Bundle.ACTIVE,
            new BundleClassLoaderTrackerCustomizer());
        classResolverBundleTracker.open();
        fieldValueBundleTracker = new BundleTracker(context, Bundle.ACTIVE,
            new FieldValueFactoryBundleCustomizer());
        fieldValueBundleTracker.open();
    }

    public void stop(BundleContext context) throws Exception {
        fieldValueBundleTracker.close();
        classResolverBundleTracker.close();
        applicationTracker.close();
    }

}
