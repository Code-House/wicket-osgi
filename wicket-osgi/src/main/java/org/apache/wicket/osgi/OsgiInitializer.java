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

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.application.IClassResolver;
import org.apache.wicket.injection.CompoundFieldValueFactory;
import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.osgi.classloader.CompoundClassResolver;
import org.apache.wicket.osgi.classloader.OsgiClassResolver;
import org.apache.wicket.osgi.ioc.OsgiFieldValueFactory;
import org.apache.wicket.osgi.ioc.OsgiInjector;
import org.apache.wicket.osgi.tracking.ApplicationClassLoaderCustomizer;
import org.apache.wicket.osgi.tracking.FieldValueFactoryServiceCustomizer;
import org.apache.wicket.settings.IApplicationSettings;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Wicket initializer which prepare application for running under OSGi.
 * 
 * Steps taken by initializer:
 * - Obtain application bundle
 * - Override classloader
 * - Add custom injector
 * - Register IClassResolver tracker
 * - Register IFieldValueFactory tracker
 */
public class OsgiInitializer implements IInitializer {

    public static final MetaDataKey<BundleContext> BUNDLE_CONTEXT = new MetaDataKey<BundleContext>() {
        private static final long serialVersionUID = 1L;
    };

    public static final MetaDataKey<Bundle> BUNDLE = new MetaDataKey<Bundle>() {
        private static final long serialVersionUID = 1L;
    };

    public static final MetaDataKey<ServiceTracker> CLASSLOADER_SERVICE_TRACKER = new MetaDataKey<ServiceTracker>() {
        private static final long serialVersionUID = 1L;
    };

    public static final MetaDataKey<ServiceTracker> FIELD_VALUE_SERVICE_TRACKER = new MetaDataKey<ServiceTracker>() {
        private static final long serialVersionUID = 1L;
    };

    public void init(Application application) {
        Bundle bundle = FrameworkUtil.getBundle(application.getClass());

        if (bundle != null) {
            application.setMetaData(BUNDLE_CONTEXT, bundle.getBundleContext());
            application.setMetaData(BUNDLE, bundle);
            CompoundClassResolver resolver = setClassLoader(application.getApplicationSettings(), bundle.getBundleContext());
            CompoundFieldValueFactory fieldValueFactory = createFieldValueFactory(bundle.getBundleContext());
            application.getComponentInstantiationListeners().add( new OsgiInjector(application, fieldValueFactory));
            registerClassResolverTracker(application, resolver, bundle.getBundleContext());
            registerFieldValueFactoryTracker(application, fieldValueFactory, bundle.getBundleContext());
        }

    }

    private void registerFieldValueFactoryTracker(Application application, CompoundFieldValueFactory fieldValueFactory, BundleContext bundleContext) {
        ServiceTracker tracker = new ServiceTracker(bundleContext, IFieldValueFactory.class.getName(), new FieldValueFactoryServiceCustomizer(fieldValueFactory, bundleContext));
        application.setMetaData(FIELD_VALUE_SERVICE_TRACKER, tracker);
        tracker.open();
    }

    private CompoundFieldValueFactory createFieldValueFactory(BundleContext context) {
        return new CompoundFieldValueFactory(new IFieldValueFactory[] {new OsgiFieldValueFactory(context)});
    }

    private void registerClassResolverTracker(Application application, CompoundClassResolver resolver, BundleContext bundleContext) {
        ServiceTracker tracker = new ServiceTracker(bundleContext, IClassResolver.class.getName(), new ApplicationClassLoaderCustomizer(resolver, bundleContext));
        application.setMetaData(CLASSLOADER_SERVICE_TRACKER, tracker);
        tracker.open();
    }

    private CompoundClassResolver setClassLoader(IApplicationSettings settings, BundleContext bundleContext) {
        IClassResolver classResolver = settings.getClassResolver();
        CompoundClassResolver compound = null;
        if (classResolver instanceof CompoundClassResolver) {
            compound = (CompoundClassResolver) classResolver;
        } else {
            compound = new CompoundClassResolver();
            compound.add(classResolver);
        }
        compound.add(new OsgiClassResolver(bundleContext.getBundle()));
        settings.setClassResolver(compound);
        return compound;
    }

    public void destroy(Application application) {
        if (application.getMetaData(CLASSLOADER_SERVICE_TRACKER) != null) {
            application.getMetaData(CLASSLOADER_SERVICE_TRACKER).close();
        }
        if (application.getMetaData(FIELD_VALUE_SERVICE_TRACKER) != null) {
            application.getMetaData(FIELD_VALUE_SERVICE_TRACKER).close();
        }
    }

    public static BundleContext getBundleContext() {
        return Application.get().getMetaData(BUNDLE_CONTEXT);
    }

}
