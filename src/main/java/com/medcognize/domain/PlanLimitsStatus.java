package com.medcognize.domain;

import com.medcognize.MedcognizeUI;
import com.medcognize.util.UserUtil;
import com.medcognize.view.crud.DisplayFriendlyTable;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.fields.MTable;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanLimitsStatus implements Serializable {

    // in == in Network
    // oon == out Of Network
    Plan plan;
    User user;

    Map<FamilyMember, Double> inDeductibleIndividualLimitMap = new HashMap<>();
    Map<FamilyMember, Double> oonDeductibleIndividualLimitMap = new HashMap<>();
    Map<FamilyMember, Double> oonOutOfPocketIndividualLimitMap = new HashMap<>();

    PlanLimit inDeductibleFamilyLimitStatus;
    PlanLimit oonDeductibleFamilyLimitStatus;
    PlanLimit oonOutOfPocketFamilyLimitStatus;

    double inDeductibleFamilyLimitUsage;
    double oonDeductibleFamilyLimitUsage;
    double oonOutOfPocketFamilyLimitUsage;

    final List<MedicalExpense> expenses;
    final List<FamilyMember> familyMembers;

    public PlanLimitsStatus(User u, Plan p) {
        this.plan = p;
        this.user = u;
        expenses = UserUtil.getMedicalExpensesForPlan(u, p);
        familyMembers = UserUtil.getFamilyMembersWithPlanExpenses(u, p);
        for (FamilyMember fm : familyMembers) {
            inDeductibleIndividualLimitMap.put(fm, (double) 0);
            oonDeductibleIndividualLimitMap.put(fm, (double) 0);
            oonOutOfPocketIndividualLimitMap.put(fm, (double) 0);
        }

        for (MedicalExpense me : expenses) {
            FamilyMember fm = me.getFamilyMember();
            if (me.isMedicalExpenseInPlan()) {
                inDeductibleFamilyLimitUsage += me.getDeductibleAmount();
                Double d = inDeductibleIndividualLimitMap.get(fm) + me.getDeductibleAmount();
                inDeductibleIndividualLimitMap.put(fm, d);
            } else {
                oonDeductibleFamilyLimitUsage += me.getDeductibleAmount();
                Double d = oonDeductibleIndividualLimitMap.get(fm) + me.getDeductibleAmount();
                oonDeductibleIndividualLimitMap.put(fm, d);
            }
            oonOutOfPocketFamilyLimitUsage += me.getOutOfPocketAmount();
            Double d = oonOutOfPocketIndividualLimitMap.get(fm) + me.getOutOfPocketAmount();
            oonOutOfPocketIndividualLimitMap.put(fm, d);
        }
        inDeductibleFamilyLimitStatus = new PlanLimit("In Network Deductible", p.getFamilyInNetworkDeductible(),
                inDeductibleFamilyLimitUsage);
        oonDeductibleFamilyLimitStatus = new PlanLimit("Out of Network Deductible",
                p.getFamilyOutOfNetworkDeductible(), oonDeductibleFamilyLimitUsage);
        oonOutOfPocketFamilyLimitStatus = new PlanLimit("Out of Pocket Expenses", p.getFamilyOutOfPocketLimit(),
                oonOutOfPocketFamilyLimitUsage);
    }

    public List<PlanLimit> getIndividualLimitsStatusContainer(FamilyMember fm) {
        double inDeductibleIndividualLimitUsage = inDeductibleIndividualLimitMap.get(fm);
        double oonDeductibleIndividualLimitUsage = oonDeductibleIndividualLimitMap.get(fm);
        double oonOutOfPocketIndividualLimitUsage = oonOutOfPocketIndividualLimitMap.get(fm);

        PlanLimit inDeductibleIndividualLimitStatus = new PlanLimit("In Network Deductible",
                plan.getIndividualInNetworkDeductible(), inDeductibleIndividualLimitUsage);
        PlanLimit oonDeductibleIndividualLimitStatus = new PlanLimit("Out of Network Deductible",
                plan.getIndividualOutOfNetworkDeductible(), oonDeductibleIndividualLimitUsage);
        PlanLimit oonOutOfPocketIndividualLimitStatus = new PlanLimit("Out of Pocket Deductible",
                plan.getIndividualOutOfPocketLimit(), oonOutOfPocketIndividualLimitUsage);

        List<PlanLimit> individualLimits = new ArrayList<>();
        individualLimits.add(inDeductibleIndividualLimitStatus);
        individualLimits.add(oonDeductibleIndividualLimitStatus);
        individualLimits.add(oonOutOfPocketIndividualLimitStatus);
        return individualLimits;
    }

    public List<PlanLimit> getFamilyLimits() {
        List<PlanLimit> familyLimits = new ArrayList<>();
        familyLimits.add(inDeductibleFamilyLimitStatus);
        familyLimits.add(oonDeductibleFamilyLimitStatus);
        familyLimits.add(oonOutOfPocketFamilyLimitStatus);
        return familyLimits;
    }

    public List<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public VerticalLayout getLimitsReport(FamilyMember fm) {
        VerticalLayout tables = new VerticalLayout();
        //tables.setSizeUndefined();
        tables.setSpacing(true);
        Table familyTable = getPlanLimitsTable(true, null);
        familyTable.setCaption("Family Limits");
        tables.addComponent(familyTable);
        Table fmTable = getPlanLimitsTable(false, fm);
        if (null == fm) {
            fmTable.setCaption("Unspecified Family Member Limits");
        } else {
            fmTable.setCaption(fm.getFamilyMemberName() + " Limits");
        }
        tables.addComponent(fmTable);
        return tables;
    }

    public VerticalLayout getLimitsReportAllFamilyMembers() {
        VerticalLayout tables = new VerticalLayout();
        //tables.setSizeUndefined();
        tables.setSpacing(true);
        Table familyTable = getPlanLimitsTable(true, null);
        familyTable.setCaption("Family Limits");
        tables.addComponent(familyTable);
        List<FamilyMember> familyMemberList = this.getFamilyMembers();
        for (FamilyMember fm : familyMemberList) {
            Table fmTable = getPlanLimitsTable(false, fm);
            if (null == fm) {
                fmTable.setCaption("Unspecified Family Member Limits");
            } else {
                fmTable.setCaption(fm.getFamilyMemberName() + " Limits");
            }
            tables.addComponent(fmTable);
        }
        return tables;
    }

    // we check specifically if we want familyLimits or not because a null FamilyMember is a
    // legitimate FamilyMember that hasn't been specified yet.
    public Table getPlanLimitsTable(boolean familyLimits, FamilyMember fm) {
        ArrayList<String> pids = new ArrayList<String>() {
            {
                add("limitName");
                add("annualLimit");
                add("usage");
                add("balance");
            }
        };
        MTable<PlanLimit> table = new MTable<>(PlanLimit.class).withProperties(pids);
        if (familyLimits) {
            table.setData(this.getFamilyLimits());
        } else {
            table.setData(this.getIndividualLimitsStatusContainer(fm));
        }
        table.setLocale(MedcognizeUI.LOCALE);
        table.setConverter("annualLimit", new StringToDoubleConverter() {
            @Override
            protected NumberFormat getFormat(java.util.Locale locale) {
                return NumberFormat.getCurrencyInstance(locale);
            }
        });
        table.setConverter("usage", new StringToDoubleConverter() {
            @Override
            protected NumberFormat getFormat(java.util.Locale locale) {
                return NumberFormat.getCurrencyInstance(locale);
            }
        });
        table.setConverter("balance", new StringToDoubleConverter() {
            @Override
            protected NumberFormat getFormat(java.util.Locale locale) {
                return NumberFormat.getCurrencyInstance(locale);
            }
        });
        table.setHeight("100px");
        return table;
    }
}
