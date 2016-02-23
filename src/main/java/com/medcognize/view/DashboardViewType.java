package com.medcognize.view;

import com.medcognize.view.admin.AdminView;
import com.medcognize.view.crud.FamilyMemberView;
import com.medcognize.view.crud.FsaView;
import com.medcognize.view.crud.MedicalExpenseView;
import com.medcognize.view.crud.PlanView;
import com.medcognize.view.crud.ProviderView;
import com.medcognize.view.dashboard.DashboardView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum DashboardViewType {
    // for icons see: https://fortawesome.github.io/Font-Awesome/icons/
    DASHBOARD(DashboardView.NAME, DashboardView.class, FontAwesome.HOME, true),
    PLAN(PlanView.NAME, PlanView.class, FontAwesome.PRODUCT_HUNT, true),
    FSA(FsaView.NAME, FsaView.class, FontAwesome.CREDIT_CARD, true),
    FAMILY(FamilyMemberView.NAME, FamilyMemberView.class, FontAwesome.USERS, true),
    PROVIDER(ProviderView.NAME, ProviderView.class, FontAwesome.MEDKIT, true),
    EXPENSES(MedicalExpenseView.NAME, MedicalExpenseView.class, FontAwesome.MONEY, true),
    ADMIN(AdminView.NAME, AdminView.class, FontAwesome.LOCK, true);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private DashboardViewType(final String viewName,
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

    public static DashboardViewType getByViewName(final String viewName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}