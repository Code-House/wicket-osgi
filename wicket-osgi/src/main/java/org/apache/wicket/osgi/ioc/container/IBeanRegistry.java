package org.apache.wicket.osgi.ioc.container;

public interface IBeanRegistry {

    <T> T getBean(String name, Class<T> type);

}
