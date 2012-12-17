package org.apache.wicket.osgi;

import java.util.Hashtable;

import org.apache.wicket.application.IClassResolver;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public class BundleClassLoaderTrackerCustomizer implements BundleTrackerCustomizer {

    @SuppressWarnings("rawtypes")
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        String imports = (String) bundle.getHeaders().get(Constants.IMPORT_PACKAGE);
        if (imports != null && imports.contains("org.apache.wicket")) {
            OsgiClassResolver service = new OsgiClassResolver(bundle.getBundleContext());
            return bundle.getBundleContext().registerService(IClassResolver.class.getName(), service, new Hashtable());
        }
        return null;
    }

    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {

    }

    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        ((ServiceRegistration) object).unregister();
    }

}
