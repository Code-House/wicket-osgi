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
package org.apache.wicket.osgi;

import org.apache.wicket.osgi.http.LazyHttpService;
import org.apache.wicket.osgi.tracking.BundleClassLoaderTrackerCustomizer;
import org.apache.wicket.osgi.tracking.FieldValueFactoryBundleCustomizer;
import org.apache.wicket.osgi.tracking.HttpServiceTrackerCustomizer;
import org.apache.wicket.osgi.tracking.WebApplicationFactoryTrakcerCustomizer;
import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

    private ServiceTracker applicationTracker;
    private BundleTracker classResolverBundleTracker;
    private BundleTracker fieldValueBundleTracker;
    private ServiceTracker httpServiceTracker;

    public void start(BundleContext context) throws Exception {
        LazyHttpService httpService = new LazyHttpService();
        httpServiceTracker = new ServiceTracker(context, HttpService.class.getName(),
            new HttpServiceTrackerCustomizer(httpService, context));
        httpServiceTracker.open();
        applicationTracker = new ServiceTracker(context, IWebApplicationFactory.class.getName(),
            new WebApplicationFactoryTrakcerCustomizer(httpService, context));
        applicationTracker.open();
        classResolverBundleTracker = new BundleTracker(context, Bundle.ACTIVE,
            new BundleClassLoaderTrackerCustomizer());
        classResolverBundleTracker.open();
        fieldValueBundleTracker = new BundleTracker(context, Bundle.ACTIVE,
            new FieldValueFactoryBundleCustomizer());
        fieldValueBundleTracker.open();
    }

    public void stop(BundleContext context) throws Exception {
        fieldValueBundleTracker.close();
        classResolverBundleTracker.close();
        applicationTracker.close();
        httpServiceTracker.close();
    }

}
