package org.code_house.wicket.test;

import org.apache.wicket.protocol.http.WebApplication;

public class MyApplication extends WebApplication {

    @Override
    protected void init() {
        super.init();
//
        mountPage("/", Home.class);
//        mountPage("/CopyOfHome", CopyOfHome.class);
//        OsgiApplicationSettings.get().setBeanRegistry(new BlueprintBeanRegistry());
    }

    public Class getHomePage() {
        return Home.class;
    }

}
