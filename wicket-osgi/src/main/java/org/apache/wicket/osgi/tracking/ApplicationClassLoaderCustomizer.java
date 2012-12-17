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

import org.apache.wicket.application.CompoundClassResolver;
import org.apache.wicket.application.IClassResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ApplicationClassLoaderCustomizer implements ServiceTrackerCustomizer {

    private final CompoundClassResolver resolver;

    private final BundleContext bundleContext;

    public ApplicationClassLoaderCustomizer(CompoundClassResolver resolver, BundleContext bundleContext) {
        this.resolver = resolver;
        this.bundleContext = bundleContext;
    }

    public void removedService(ServiceReference reference, Object service) {
        resolver.remove((IClassResolver) service);
    }

    public void modifiedService(ServiceReference reference, Object service) {
    }

    public Object addingService(ServiceReference reference) {
        IClassResolver service = (IClassResolver) bundleContext.getService(reference);
        resolver.add(service);
        return service;
    }

}