package org.code_house.examples.multibundle.extension;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.protocol.http.WebApplication;

public class ExtensionInitializer implements IInitializer {

    public void init(Application application) {
        if (application instanceof WebApplication) {
            ((WebApplication) application).mountPage("/extension", ExtensionPage.class);
        }
    }

    public void destroy(Application application) {
        if (application instanceof WebApplication) {
            ((WebApplication) application).unmount("/extension");
        }
    }

}
