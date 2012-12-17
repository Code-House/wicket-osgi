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

import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.protocol.http.WicketServlet;

/**
 * Extension of {@link WicketServlet} which takes given instance of application.
 */
public class WicketOsgiServlet extends WicketServlet {

    private static final long serialVersionUID = 1L;

    private IWebApplicationFactory factory;

    private final String path;

    public WicketOsgiServlet(String path, IWebApplicationFactory factory) {
        this.path = path;
        this.factory = factory;
    }

    @Override
    protected WicketFilter newWicketFilter() {
        WicketFilter filter = new WicketOsgiFilter(factory);
        filter.setFilterPath(path);
        return filter;
    }

}
