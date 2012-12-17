package org.apache.wicket.osgi;

import org.apache.wicket.injection.CompoundFieldValueFactory;
import org.apache.wicket.injection.IFieldValueFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class FieldValueFactoryServiceCustomizer implements ServiceTrackerCustomizer {

    private final CompoundFieldValueFactory fieldValueFactory;
    private final BundleContext bundleContext;

    public FieldValueFactoryServiceCustomizer(CompoundFieldValueFactory fieldValueFactory, BundleContext bundleContext) {
        this.fieldValueFactory = fieldValueFactory;
        this.bundleContext = bundleContext;
    }

    public Object addingService(ServiceReference reference) {
        fieldValueFactory.addFactory((IFieldValueFactory) bundleContext.getService(reference));
        return true;
    }

    public void modifiedService(ServiceReference reference, Object service) {
        // TODO Auto-generated method stub

    }

    public void removedService(ServiceReference reference, Object service) {
        bundleContext.ungetService(reference);
        // TODO suport removal of field value factories
    }

}
