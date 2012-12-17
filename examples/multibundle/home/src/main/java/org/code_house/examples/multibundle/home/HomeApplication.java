package org.code_house.examples.multibundle.home;

import org.apache.wicket.Page;
import org.apache.wicket.devutils.inspector.RenderPerformanceListener;
import org.apache.wicket.protocol.http.WebApplication;

public class HomeApplication extends WebApplication {

    @Override
    protected void init() {
        initializeComponents();

        mountPage("/", HomePage.class);

        getComponentInstantiationListeners().add(new RenderPerformanceListener());
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

}
