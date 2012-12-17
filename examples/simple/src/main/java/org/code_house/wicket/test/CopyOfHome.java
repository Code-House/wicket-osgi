package org.code_house.wicket.test;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class CopyOfHome extends Home {

    public CopyOfHome(PageParameters params) {
        super(params);

        Link link = new Link("link") {
            @Override
            public void onClick() {
                setResponsePage(Home.class);
            }
        };
        link.setModel(new Model("Home"));
        addOrReplace(link);
    }

}
