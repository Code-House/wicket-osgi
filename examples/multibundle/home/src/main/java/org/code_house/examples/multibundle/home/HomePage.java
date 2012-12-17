package org.code_house.examples.multibundle.home;

import org.apache.wicket.request.cycle.RequestCycle;

public class HomePage extends BasePage {

    private static final long serialVersionUID = 1L;

    public HomePage() {
        System.out.println(RequestCycle.get().getUrlRenderer().getBaseUrl());
    }

}
