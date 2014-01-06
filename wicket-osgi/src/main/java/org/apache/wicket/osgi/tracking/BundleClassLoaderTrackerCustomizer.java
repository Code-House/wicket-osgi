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

import java.util.Hashtable;

import org.apache.wicket.application.IClassResolver;
import org.apache.wicket.osgi.classloader.OsgiClassResolver;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public class BundleClassLoaderTrackerCustomizer implements BundleTrackerCustomizer {

    @SuppressWarnings("rawtypes")
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        String imports = (String) bundle.getHeaders().get(Constants.IMPORT_PACKAGE);
        if (imports != null && imports.contains("org.apache.wicket")) {
            OsgiClassResolver service = new OsgiClassResolver(bundle);
            return bundle.getBundleContext().registerService(IClassResolver.class.getName(), service, new Hashtable());
        }
        return null;
    }

    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {

    }

    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        ((ServiceRegistration) object).unregister();
    }

}
