package org.code_house.examples.multibundle.osgi;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.protocol.http.WebApplication;

public class OsgiInitializer implements IInitializer {

    public void init(Application application) {
        if (application instanceof WebApplication) {
            ((WebApplication) application).mountPage("/osgi", OsgiPage.class);
        }
    }

    public void destroy(Application application) {
        if (application instanceof WebApplication) {
            ((WebApplication) application).unmount("/osgi");
        }
    }

}
