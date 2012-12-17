package org.apache.wicket.osgi;

import java.util.Hashtable;

import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.osgi.ioc.OsgiFieldValueFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public class FieldValueFactoryBundleCustomizer implements BundleTrackerCustomizer {

    public Object addingBundle(Bundle bundle, BundleEvent event) {
        String imports = (String) bundle.getHeaders().get(Constants.IMPORT_PACKAGE);
        if (imports != null && imports.contains("org.apache.wicket")) {
            IFieldValueFactory service = new OsgiFieldValueFactory(bundle.getBundleContext());
            return bundle.getBundleContext().registerService(IFieldValueFactory.class.getName(), service, new Hashtable());
        }
        return null;
    }

    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
        
    }

    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        ((ServiceRegistration) object).unregister();
    }

}
