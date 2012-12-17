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
package org.apache.wicket.osgi.ioc.container.blueprint;

import org.apache.wicket.osgi.ioc.container.IBeanRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.osgi.service.blueprint.container.NoSuchComponentException;

public class BlueprintBeanRegistry implements IBeanRegistry {

    private final Long bundleId;

    public BlueprintBeanRegistry(Long bundleId) {
        this.bundleId = bundleId;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(String name, Class<T> type) {
        BundleContext bundleContext = getBundleContext(bundleId);
        ServiceReference reference = getBlueprintContainer(bundleContext);
        if (reference != null) {
            BlueprintContainer container = (BlueprintContainer) bundleContext.getService(reference);
            try {
                Object componentInstance = container.getComponentInstance(name);

                if (type.isAssignableFrom(componentInstance.getClass())) {
                    return (T) componentInstance;
                }
            } catch (NoSuchComponentException e) {
                // TODO handle fault
            } finally {
                bundleContext.ungetService(reference);
            }
        }
        return null;
    }

    private BundleContext getBundleContext(Long bundleId) {
        Bundle bundle = FrameworkUtil.getBundle(getClass()); // should return system bundle
        return bundle.getBundleContext().getBundle(bundleId).getBundleContext();
    }

    private ServiceReference getBlueprintContainer(BundleContext context) {
        Bundle bundle = context.getBundle();

        String filter = String.format("(&(%s=%s)(%s=%s))", "osgi.blueprint.container.symbolicname",
            bundle.getSymbolicName(),
            "osgi.blueprint.container.version",
            bundle.getVersion()
        );

        try {
            ServiceReference[] references = context.getAllServiceReferences(BlueprintContainer.class.getName(), filter);
            if (references != null && references.length == 1) {
                return references[0];
            }
        } catch (InvalidSyntaxException e) {
        }

        return null;
    }

    

}
