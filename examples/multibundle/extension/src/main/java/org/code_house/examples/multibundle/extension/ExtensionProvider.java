package org.code_house.examples.multibundle.extension;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.code_house.examples.multibundle.api.LinkProvider;

public class ExtensionProvider implements LinkProvider {

    public Link<?> createPageLink(String linkId, String labelId) {
        BookmarkablePageLink<ExtensionPage> link = new BookmarkablePageLink<ExtensionPage>(linkId, ExtensionPage.class);
        link.add(new Label(labelId, "Extension"));
        return link;
    }

}
