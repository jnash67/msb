package com.medcognize.util;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.MedicalExpense;
import com.vaadin.ui.Notification;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class DChartsUtil implements Serializable {
    private static Calendar cal = Calendar.getInstance();

    public static ArrayList<Double> getMedicalExpensesForFamilyMemberByMonth(FamilyMember fm, List<MedicalExpense> expensesForPlan) {
        List<MedicalExpense> mes = DChartsUtil.getMedicalExpensesForFamilyMember(fm, expensesForPlan);
        return medicalExpensesByMonth(mes, 0);
    }

    public static List<MedicalExpense> getMedicalExpensesForFamilyMember(FamilyMember fm, List<MedicalExpense> expensesForPlan) {
        List<MedicalExpense> expensesForFamilyMemberInPlan = new ArrayList<>();
        if (null == fm) {
            return expensesForFamilyMemberInPlan;
        }
        for (MedicalExpense me : expensesForPlan) {
            if (fm.equals(me.getFamilyMember())) {
                expensesForFamilyMemberInPlan.add(me);
            }
        }
        return expensesForFamilyMemberInPlan;
    }

    public static ArrayList<Double> medicalExpensesByMonth(List<MedicalExpense> expenses, int metricIndex) {
        double[] exp = new double[12];
        int month;
        for (MedicalExpense me : expenses) {
            cal.setTime(me.getDate());
            month = cal.get(Calendar.MONTH);
            if (0 == metricIndex) {
                exp[month] += me.getCostAccordingToProvider();
            } else if (1 == metricIndex) {
                exp[month] += me.getOutOfPocketAmount();
            } else {
                exp[month] += me.getDeductibleAmount();
            }
        }
        ArrayList<Double> retVal = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            retVal.add(i, exp[i]);
        }
        return retVal;
    }
}
