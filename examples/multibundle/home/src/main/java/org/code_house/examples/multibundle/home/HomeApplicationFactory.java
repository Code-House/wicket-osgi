package org.code_house.examples.multibundle.home;

import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;

public class HomeApplicationFactory implements IWebApplicationFactory {

    public WebApplication createApplication(WicketFilter filter) {
        return new HomeApplication();
    }

    public void destroy(WicketFilter filter) {
    }

}
