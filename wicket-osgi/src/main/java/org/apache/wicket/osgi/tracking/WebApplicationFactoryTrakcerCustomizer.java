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

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.wicket.osgi.WicketOsgiServlet;
import org.apache.wicket.osgi.factory.WrapperWebApplicationFactory;
import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WicketServlet;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebApplicationFactoryTrakcerCustomizer implements ServiceTrackerCustomizer {

    private Logger logger = LoggerFactory.getLogger(WebApplicationFactoryTrakcerCustomizer.class);
    private final BundleContext context;
    private HttpService httpService;
    private List<String> reservedNames = Arrays.asList(
        Constants.OBJECTCLASS,
        Constants.SERVICE_ID,
        Constants.SERVICE_PID,
        Constants.SERVICE_RANKING,
        Constants.SERVICE_DESCRIPTION,
        "mount-point",
        "application-name"
    );

    public WebApplicationFactoryTrakcerCustomizer(HttpService httpService, BundleContext context) {
        this.httpService = httpService;
        this.context = context;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object addingService(ServiceReference reference) {
        //Bundle bundle = reference.getBundle();

        String path = (String) reference.getProperty("mount-point");
        String applicationName = (String) reference.getProperty("application-name");
        IWebApplicationFactory service = (IWebApplicationFactory) context.getService(reference);
        if (service != null) {
            //dirtyClassLoaderHack(service, bundle);
            WicketServlet wicketServlet = new WicketOsgiServlet(path, new WrapperWebApplicationFactory(service));

            Dictionary properties = new Hashtable();
            for (String property : reference.getPropertyKeys()) {
                if (isAllowedPropertyName(property)) {
                    properties.put(property, reference.getProperty(property));
                }
            }

            properties.put("servlet-name", applicationName);
            HttpContext defaultHttpContext = httpService.createDefaultHttpContext();
            try {
                httpService.registerServlet(path, wicketServlet,
                    properties, defaultHttpContext);
                return path;
            } catch (ServletException e) {
                logger.error("Can not register servlet", e);
            } catch (NamespaceException e) {
                logger.error("Can not register servlet", e);
            }
        }
        return null;
    }

    private boolean isAllowedPropertyName(String property) {
        return !reservedNames.contains(property);
    }

    public void modifiedService(ServiceReference reference, Object service) {
        removedService(reference, service);
        service = addingService(reference);
    }

    public void removedService(ServiceReference reference, Object service) {
        httpService.unregister((String) service);
    }

}
