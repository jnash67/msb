package com.medcognize.view.dashboard.widget;

import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.data.PieChartData;
import at.downdrown.vaadinaddons.highchartsapi.model.series.PieChartSeries;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

public class HighChartWidget extends ContentWidget {

	HighChart highchartsPie;
    final String caption;

    public HighChartWidget(final String caption, VerticalLayout root, CssLayout dashboardPanels) {
        super(root, dashboardPanels);
        this.caption = caption;

        ChartConfiguration pieConfiguration = new ChartConfiguration();
        pieConfiguration.setTitle("TestPie");
        pieConfiguration.setChartType(ChartType.PIE);
        pieConfiguration.setBackgroundColor(Colors.WHITE);

        PieChartSeries pieFruits  = new PieChartSeries("Fruits");
        PieChartData bananas = new PieChartData("Bananas", 2);
        PieChartData melons = new PieChartData("Melons", 3);
        PieChartData apples = new PieChartData("Apples", 95);

        pieFruits.getData().add(bananas);
        pieFruits.getData().add(melons);
        pieFruits.getData().add(apples);

        pieConfiguration.getSeriesList().add(pieFruits);
        try {
            highchartsPie = HighChartFactory.renderChart(pieConfiguration);
            highchartsPie.setWidth("400px");
            highchartsPie.setHeight("300px");
            System.out.println("PieChart Script : " + pieConfiguration.getHighChartValue());
            setContent(highchartsPie);
        } catch (HighChartsException e) {
            e.printStackTrace();
        }
    }
}
