package org.apache.wicket.osgi;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.protocol.http.WicketServlet;

public class WicketOsgiServlet extends WicketServlet {

    private static final long serialVersionUID = -4421915165848855329L;

    private WebApplication application;

    private final String path;

    public WicketOsgiServlet(String path, WebApplication application) {
        this.path = path;
        this.application = application;
    }

    @Override
    protected WicketFilter newWicketFilter() {
        WicketFilter filter = new WicketFilter(application);
        filter.setFilterPath("/" + path);
        return filter;
    }

}
