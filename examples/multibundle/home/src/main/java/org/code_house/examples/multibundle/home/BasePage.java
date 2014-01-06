package org.code_house.examples.multibundle.home;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.code_house.examples.multibundle.api.LinkProvider;

public class BasePage extends WebPage {

    private static final long serialVersionUID = 1L;

    @Inject @Named("providers")
    private List<LinkProvider> providers;

    public BasePage() {
        RepeatingView repeatingView = new RepeatingView("item");
        for (LinkProvider provider : providers) {
            WebMarkupContainer container = new WebMarkupContainer(repeatingView.newChildId());
            container.add(provider.createPageLink("link", "label"));
            repeatingView.add(container);
        }
        add(repeatingView);

        add(new Label("class", getClass().getName()));
    }

}
