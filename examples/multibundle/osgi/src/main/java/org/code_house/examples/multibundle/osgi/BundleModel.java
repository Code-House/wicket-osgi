package org.code_house.examples.multibundle.osgi;

import org.apache.wicket.model.LoadableDetachableModel;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;


public class BundleModel extends LoadableDetachableModel<Bundle> {

    private BundleContext context;
    private long id;

    public BundleModel(Bundle object, BundleContext context) {
        super(object);
        this.context = context;
        this.id = object.getBundleId();
    }

    @Override
    protected Bundle load() {
        return context.getBundle(id);
    }


}
