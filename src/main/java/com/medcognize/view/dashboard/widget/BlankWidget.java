package com.medcognize.view.dashboard.widget;

import com.medcognize.view.dashboard.DashboardView;
import com.vaadin.ui.VerticalLayout;

public class BlankWidget extends PanelWidget {

    public BlankWidget(DashboardView view) {
        super(new VerticalLayout(), view);
        getContent().addStyleName("plain");
    }
}
