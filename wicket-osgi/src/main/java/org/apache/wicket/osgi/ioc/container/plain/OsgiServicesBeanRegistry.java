package org.apache.wicket.osgi.ioc.container.plain;

import org.apache.wicket.Application;
import org.apache.wicket.osgi.OsgiInitializer;
import org.apache.wicket.osgi.ioc.OsgiServiceLookup;
import org.apache.wicket.osgi.ioc.container.IBeanRegistry;
import org.osgi.framework.BundleContext;

public class OsgiServicesBeanRegistry implements IBeanRegistry {

    private BundleContext context;

    public OsgiServicesBeanRegistry() {
        this.context = Application.get().getMetaData(OsgiInitializer.BUNDLE_CONTEXT);
    }

    public <T> T getBean(String name, Class<T> type) {
        return OsgiServiceLookup.getOsgiService(context, type);
    }

}
