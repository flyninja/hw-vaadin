package com.togacure;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * @author Vitaly Alekseev
 * @since 10.02.2021
 */
@Route
public class MainView extends VerticalLayout {

    public MainView() {
        add(new Text("Hello World!"));
    }

}
