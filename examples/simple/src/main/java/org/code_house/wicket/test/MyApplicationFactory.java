package org.code_house.wicket.test;

import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;

public class MyApplicationFactory implements IWebApplicationFactory {

    public WebApplication createApplication(WicketFilter filter) {
        return new MyApplication();
    }

    public void destroy(WicketFilter filter) {
        
    }

}
