/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
