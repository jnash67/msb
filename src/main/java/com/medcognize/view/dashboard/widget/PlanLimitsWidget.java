package com.medcognize.view.dashboard.widget;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.PlanLimitsStatus;
import com.medcognize.domain.User;
import com.medcognize.view.dashboard.DashboardView;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

public class PlanLimitsWidget extends PanelWidget {

    final PlanLimitsStatus pls;
    FamilyMember fm;
    ComboBox familyMemberSelect;
    Label familyTableCaption, familyMemberTableCaption;
    Table familyTable, familyMemberTable;
    VerticalLayout combined = new VerticalLayout();
    VerticalLayout ftPlusCaption;

    public PlanLimitsWidget(User u, DashboardView view) {
        super(view);
        pls = new PlanLimitsStatus(u, u.getRepo().getActivePlan(u));
        List<FamilyMember> fms = pls.getFamilyMembers();
        if (0 == fms.size()) {
            familyMemberSelect = new ComboBox("");
            familyMemberSelect.setEnabled(false);
        } else {
            familyMemberSelect = new ComboBox("", fms);
            fm = fms.get(0);
            familyMemberSelect.select(fm);
            familyMemberSelect.setImmediate(true);
            if (!fms.contains(null)) {
                familyMemberSelect.setNullSelectionAllowed(false);
            }
            familyMemberSelect.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    fm = (FamilyMember) familyMemberSelect.getValue();
                    createLayout();
                }
            });
        }
        familyMemberSelect.setTextInputAllowed(false);
        combined.setCaption("Active Plan Limit Usage");
        combined.setSpacing(true);
        createLayout();
        createContent(combined);
    }

    private void createLayout() {
        if (null == familyTable) {
            familyTable = pls.getPlanLimitsTable(true, null);
            familyTableCaption = new Label("Family Limits");
            familyTable.addStyleName("wordwrap-headers");
            formatPlanLimitsTable(familyTable);
            ftPlusCaption = new VerticalLayout();
            ftPlusCaption.setSpacing(false);
            ftPlusCaption.addComponent(familyTableCaption);
            ftPlusCaption.addComponent(familyTable);
        }

        if (null == fm) {
            familyMemberTableCaption = new Label("The active plan has no expenses specified");
        } else {
            familyMemberTable = pls.getPlanLimitsTable(false, fm);
            formatPlanLimitsTable(familyMemberTable);
            familyMemberTable.addStyleName("wordwrap-headers");
            familyMemberTableCaption = new Label(fm.getFamilyMemberName() + " Individual Limits");
        }

        VerticalLayout ftmPlusCaption = new VerticalLayout();
        ftmPlusCaption.setSpacing(false);
        ftmPlusCaption.addComponent(familyMemberTableCaption);
        if (null != fm) {
            ftmPlusCaption.addComponent(familyMemberTable);
        }

        combined.removeAllComponents();
        combined.addComponent(familyMemberSelect);
        combined.addComponent(ftPlusCaption);
        combined.addComponent(ftmPlusCaption);
    }

    private void formatPlanLimitsTable(Table t) {
        t.setColumnWidth("limitName", 100);
        t.setColumnWidth("annualLimit", 75);
        t.setColumnWidth("usage", 75);
        t.setColumnWidth("balance", 75);
        t.setHeight("115px");
    }

}
