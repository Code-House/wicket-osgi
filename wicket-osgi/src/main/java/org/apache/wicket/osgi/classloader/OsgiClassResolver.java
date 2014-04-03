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
package org.apache.wicket.osgi.classloader;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.apache.wicket.application.AbstractClassResolver;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

public class OsgiClassResolver extends AbstractClassResolver {
    private Bundle bundle;

    public OsgiClassResolver(Bundle bundle) {
        this.bundle = bundle;
    }

    public ClassLoader getClassLoader() {
        return bundle.adapt(BundleWiring.class).getClassLoader();
    }

    @Override
    public Iterator<URL> getResources(String name) {
        try {
            return new EnumrationIterator<URL>(bundle.getResources(name));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "OsgiClassResolver[" + bundle.getSymbolicName() + "]";
    }
}

