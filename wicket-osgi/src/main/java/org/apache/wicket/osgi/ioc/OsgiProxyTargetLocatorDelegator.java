package org.apache.wicket.osgi.ioc;

import org.apache.wicket.osgi.ioc.container.blueprint.BlueprintBeanRegistry;
import org.apache.wicket.proxy.IProxyTargetLocator;

public class OsgiProxyTargetLocatorDelegator implements IProxyTargetLocator {

    private static final long serialVersionUID = 1l;

    private final String name;
    private final Class<?> type;
    private final Long bundleId;
    private transient Object target;

    public OsgiProxyTargetLocatorDelegator(Long bundleId, String name, Class<?> type) {
        this.bundleId = bundleId;
        this.name = name;
        this.type = type;
    }

    public Object locateProxyTarget() {
        if (target == null) {
            target = new BlueprintBeanRegistry(bundleId).getBean(name, type);
        }
        return target;
    }

}