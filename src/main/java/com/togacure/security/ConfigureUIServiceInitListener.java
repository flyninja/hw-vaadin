package com.togacure.security;

import com.togacure.views.LoginView;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

/**
 * @author Vitaly Alekseev
 * @since 10.02.2021
 */
@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(final ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addUIInitListener(uiEvent -> {
            val ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    private void beforeEnter(final BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget()) && !isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
    }

    private static boolean isUserLoggedIn() {
        val subject = SecurityUtils.getSubject();
        return subject != null && subject.isAuthenticated();
    }
}
