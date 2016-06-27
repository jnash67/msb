package com.medcognize.view;

import com.medcognize.MedcognizeUI;
import com.medcognize.util.SpringUtil;
import com.medcognize.view.dashboard.DashboardView;
import com.medcognize.view.homepage.HomepageView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import org.springframework.stereotype.Component;

@Component // No SpringView annotation because this view can not be navigated to
@UIScope
public class AccessDeniedView extends VerticalLayout implements View {

    public AccessDeniedView() {
//        setMargin(true);
//        Label lbl = new Label("You don't have access to this view.");
//        lbl.addStyleName(ValoTheme.LABEL_FAILURE);
//        lbl.setSizeUndefined();
//        addComponent(lbl);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        final MedcognizeUI ref = (MedcognizeUI) MedcognizeUI.getCurrent();
        if (SpringUtil.hasRole(SpringUtil.RoleType.ROLE_USER)) {
            ref.getNavigator().navigateTo(DashboardView.NAME);
        } else {
            ref.getNavigator().navigateTo(HomepageView.NAME);
        }
    }
}