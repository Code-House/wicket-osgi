package org.code_house.examples.multibundle.api;

import org.apache.wicket.markup.html.link.Link;

public interface LinkProvider {

    Link<?> createPageLink(String linkId, String labelId);

}
