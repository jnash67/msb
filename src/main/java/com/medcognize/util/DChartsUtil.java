package com.medcognize.util;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.MedicalExpense;
import com.vaadin.ui.Notification;
import lombok.extern.slf4j.Slf4j;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickEvent;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickHandler;
import org.dussan.vaadin.dcharts.events.mouseenter.ChartDataMouseEnterEvent;
import org.dussan.vaadin.dcharts.events.mouseenter.ChartDataMouseEnterHandler;
import org.dussan.vaadin.dcharts.events.mouseleave.ChartDataMouseLeaveEvent;
import org.dussan.vaadin.dcharts.events.mouseleave.ChartDataMouseLeaveHandler;
import org.dussan.vaadin.dcharts.events.rightclick.ChartDataRightClickEvent;
import org.dussan.vaadin.dcharts.events.rightclick.ChartDataRightClickHandler;
import org.dussan.vaadin.dcharts.options.Options;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class DChartsUtil implements Serializable {
    private static Calendar cal = Calendar.getInstance();

    public void addEventHandling(DCharts chart, Options options) {
        options.setCaptureRightClick(true);
        chart.setEnableChartDataMouseEnterEvent(true);
        chart.setEnableChartDataMouseLeaveEvent(true);
        chart.setEnableChartDataClickEvent(true);
        chart.setEnableChartDataRightClickEvent(true);
        chart.addHandler(new ChartDataMouseEnterHandler() {
            @Override
            public void onChartDataMouseEnter(ChartDataMouseEnterEvent event) {
                Notification.show("CHART DATA MOUSE ENTER", DChartsUtil.getChartDataMouseEnterEventString(event), Notification.Type.HUMANIZED_MESSAGE);
            }
        });
        chart.addHandler(new ChartDataMouseLeaveHandler() {
            @Override
            public void onChartDataMouseLeave(ChartDataMouseLeaveEvent event) {
                Notification.show("CHART DATA MOUSE LEAVE", event.getChartData().toString(), Notification.Type.HUMANIZED_MESSAGE);
            }
        });
        chart.addHandler(new ChartDataClickHandler() {
            @Override
            public void onChartDataClick(ChartDataClickEvent event) {
                Notification.show("CHART DATA CLICK", DChartsUtil.getChartDataClickEventString(event), Notification.Type.HUMANIZED_MESSAGE);
            }
        });
        chart.addHandler(new ChartDataRightClickHandler() {
            @Override
            public void onChartDataRightClick(ChartDataRightClickEvent event) {
                Notification.show("CHART DATA RIGHT CLICK", event.getChartData().toString(), Notification.Type.HUMANIZED_MESSAGE);
            }
        });
    }

    public static String getChartDataClickEventString(ChartDataClickEvent event) {
        String chartId = event.getChartData().getChartId();
        Long seriesIndex = event.getChartData().getSeriesIndex();
        Long pointIndex = event.getChartData().getPointIndex();
        Object chartData = event.getChartData().getData();
        Object originData = event.getChartData().getOriginData();
        String cd = "Chart id: " + chartId + "\n";
        cd = cd + "Series Index: " + seriesIndex + "\n";
        cd = cd + "Point Index: " + pointIndex + "\n";
        cd = cd + "Chart Data: " + chartData + "\n";
        cd = cd + "Origin Data: " + originData + "\n";
        return cd;
    }

    public static String getChartDataMouseEnterEventString(ChartDataMouseEnterEvent event) {
        String chartId = event.getChartData().getChartId();
        Long seriesIndex = event.getChartData().getSeriesIndex();
        Long pointIndex = event.getChartData().getPointIndex();
        Object originData = event.getChartData().getOriginData();
        Object chartData = event.getChartData().getData();
        ArrayList al = (ArrayList) ((Object[]) chartData)[0];
        String xVal = al.get(0).toString();
        String yVal = al.get(1).toString();
        String cd = "Chart id: " + chartId + "\n";
        cd = cd + "Series Index: " + seriesIndex + "\n";
        cd = cd + "Point Index: " + pointIndex + "\n";
        cd = cd + "Chart Data:[ " + xVal + ", " + yVal + "]\n";
        cd = cd + "Origin Data: " + originData + "\n";
        return cd;
    }

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
