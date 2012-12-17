package org.apache.wicket.osgi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Application;

public class ApplicationRegistryImpl implements IApplicationRegistry {

    private Map<String, Application> applications = Collections.synchronizedMap(new HashMap<String, Application>());

    public Application get(String name) {
        return applications.get(name);
    }

    public void set(String name, Application app) {
        applications.put(name, app);
    }

    public void remove(String name) {
        applications.remove(name);
    }

}
