package org.code_house.wicket.test;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.apache.wicket.Application;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.osgi.OsgiInitializer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.osgi.framework.Bundle;

public class Home extends WebPage {

    @Inject
    @Named("features")
    private FeaturesService service;

    public Home(PageParameters parameters) {
        Bundle bundle = Application.get().getMetaData(OsgiInitializer.BUNDLE);

        add(
           new Label("id", "" + bundle.getBundleId()),
           new Label("name", bundle.getSymbolicName())
        );

        Link link = new Link("link") {
            @Override
            public void onClick() {
                setResponsePage(CopyOfHome.class);
            }
        };
        link.setModel(new Model("CopyOfHome"));
        add(link);
        try {
            if (service != null) {
                Feature[] features = service.listFeatures();

                add(new Label("features", Arrays.toString(features)));
            } else {
                add(new Label("features", "NO SERVICE FOUND"));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
