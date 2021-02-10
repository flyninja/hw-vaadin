package com.togacure;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import lombok.val;

/**
 * @author Vitaly Alekseev
 * @since 10.02.2021
 */
@Route
public class MainView extends AppLayout {

    public MainView() {
        val logo = new H3("Hello World!");
        logo.addClassName("logo");

        val logout = new Anchor("logout", "Log out");

        val header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        addToNavbar(header);
    }

}
