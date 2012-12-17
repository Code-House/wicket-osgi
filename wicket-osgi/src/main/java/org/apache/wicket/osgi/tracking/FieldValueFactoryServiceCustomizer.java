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
package org.apache.wicket.osgi.tracking;

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
