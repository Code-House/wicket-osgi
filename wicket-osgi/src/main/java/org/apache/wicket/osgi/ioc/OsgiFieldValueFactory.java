package org.apache.wicket.osgi.ioc;

import java.lang.reflect.Field;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.proxy.IProxyTargetLocator;
import org.apache.wicket.proxy.LazyInitProxyFactory;
import org.osgi.framework.BundleContext;

public class OsgiFieldValueFactory implements IFieldValueFactory {

    private final Long bundleId;

    public OsgiFieldValueFactory(Long bundleId) {
        this.bundleId = bundleId;
    }

    public OsgiFieldValueFactory(BundleContext context) {
        this(context.getBundle().getBundleId());
    }

    public Object getFieldValue(final Field field, Object fieldOwner) {
        final Class<?> type = field.getType();

        String name = null;
        Named named = field.getAnnotation(Named.class);
        if (named != null) {
            name = named.value();
        }

        IProxyTargetLocator locator = new OsgiProxyTargetLocatorDelegator(bundleId, name, type);
        if (locator.locateProxyTarget() != null) {
            return LazyInitProxyFactory.createProxy(type, locator);
        }
        return null;
    }

    public boolean supportsField(Field field) {
        return field.isAnnotationPresent(Inject.class);
    }

}
