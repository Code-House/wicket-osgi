package org.apache.wicket.osgi.ioc;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.injection.Injector;

public class OsgiInjector extends Injector implements IComponentInstantiationListener {

    private IFieldValueFactory fieldValueFactory;

    public OsgiInjector(Application application, IFieldValueFactory fieldValueFactory) {
        bind(application);
        this.fieldValueFactory = fieldValueFactory;
    }

    @Override
    public void inject(Object object) {
        inject(object, fieldValueFactory);
    }

    public void onInstantiation(Component component) {
        inject(component);
    }

}
