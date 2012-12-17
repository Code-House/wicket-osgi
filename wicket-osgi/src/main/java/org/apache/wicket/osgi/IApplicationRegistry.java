package org.apache.wicket.osgi;

import org.apache.wicket.Application;

public interface IApplicationRegistry {

    Application get(String name);

    void set(String name, Application service);

    void remove(String name);

}
