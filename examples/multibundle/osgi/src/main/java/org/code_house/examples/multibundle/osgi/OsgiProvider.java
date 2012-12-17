package org.code_house.examples.multibundle.osgi;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.code_house.examples.multibundle.api.LinkProvider;

public class OsgiProvider implements LinkProvider {

    public Link<?> createPageLink(String linkId, String labelId) {
        BookmarkablePageLink<OsgiPage> link = new BookmarkablePageLink<OsgiPage>(linkId, OsgiPage.class);
        link.add(new Label(labelId, "OSGi"));
        return link;
    }

}
