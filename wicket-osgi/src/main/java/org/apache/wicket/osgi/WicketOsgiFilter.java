package org.apache.wicket.osgi;

import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WicketFilter;

public class WicketOsgiFilter extends WicketFilter {

    private final IWebApplicationFactory factory;

    public WicketOsgiFilter(IWebApplicationFactory factory) {
        this.factory = factory;
    }

    @Override
    protected IWebApplicationFactory getApplicationFactory() {
        return factory;
    }

}
