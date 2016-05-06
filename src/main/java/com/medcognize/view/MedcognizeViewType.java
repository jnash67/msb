package com.medcognize.view;

import com.medcognize.view.dashboard.DashboardView;
import com.medcognize.view.homepage.HomepageView;
import com.medcognize.view.homepage.LoginView;
import com.medcognize.view.homepage.RegisterView;
import com.medcognize.view.reports.ReportsView;
import com.medcognize.view.sales.SalesView;
import com.medcognize.view.schedule.ScheduleView;
import com.medcognize.view.transactions.TransactionsView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum MedcognizeViewType {
    HOME(HomepageView.NAME, HomepageView.class, FontAwesome.HOME, false),
    REGISTER(RegisterView.NAME, RegisterView.class, FontAwesome.HOME, false),
    LOGIN(LoginView.NAME, LoginView.class, FontAwesome.HOME, false),
    DASHBOARD(DashboardView.NAME, DashboardView.class, FontAwesome.HOME, true),
    SALES(SalesView.NAME, SalesView.class, FontAwesome.BAR_CHART_O, false),
    TRANSACTIONS(TransactionsView.NAME, TransactionsView.class, FontAwesome.TABLE, false),
    REPORTS(ReportsView.NAME, ReportsView.class, FontAwesome.FILE_TEXT_O, true),
    SCHEDULE(ScheduleView.NAME, ScheduleView.class, FontAwesome.CALENDAR_O, false);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private MedcognizeViewType(final String viewName,
                               final Class<? extends View> viewClass, final Resource icon,
                               final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static MedcognizeViewType getByViewName(final String viewName) {
        MedcognizeViewType result = null;
        for (MedcognizeViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}