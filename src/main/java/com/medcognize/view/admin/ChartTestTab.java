package com.medcognize.view.admin;

import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.series.AreaChartSeries;
import com.medcognize.domain.User;
import com.medcognize.util.DbUtil;
import com.medcognize.view.dashboard.widget.PlanCostsChartWidget;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import java.util.Arrays;
import java.util.List;

public class ChartTestTab extends Panel {

    public ChartTestTab() {
        super();
        User u = DbUtil.getLoggedInUser();
        //FlotChartWidget cw = new FlotChartWidget("Chart");
        //HighchartWidget hw = new HighchartWidget("Pie");
        PlanCostsChartWidget dw = new PlanCostsChartWidget(u, "Test", null, null);
        HighChart chart = createChart2HC();
        //DCharts chart = createChart2();
//        DCharts chart2 = dw.getChart();
//        DCharts chart3 = createChart3();

        //QuadrantWidget qw = new QuadrantWidget("Charts", cw, hw, dw, null);
        VerticalLayout vl = new VerticalLayout();
        vl.setSpacing(true);
        vl.setMargin(true);
        vl.addComponent(chart);
//        vl.addComponent(chart2);
//        vl.addComponent(chart3);
        setContent(vl);
    }


    public HighChart createChart2HC() {
        ChartConfiguration cc = new ChartConfiguration();
        cc.setTitle("Contribution of Urban and Rural Population to National Percentiles (edited data)");
        cc.setChartType(ChartType.AREA);
        cc.getxAxis().setTitle("Population Percentile");

        AreaChartSeries urban = new AreaChartSeries("Urban");
        AreaChartSeries rural = new AreaChartSeries("Rural");
        Double[] rarray = {0.9176, 0.9296, 0.927, 0.9251, 0.9241, 0.9225, 0.9197, 0.9164,
                0.9131, 0.9098, 0.9064, 0.9028, 0.8991, 0.8957, 0.8925, 0.8896, 0.8869, 0.8844, 0.882, 0.8797,
                0.8776, 0.8755, 0.8735, 0.8715, 0.8696, 0.8677, 0.8658, 0.8637, 0.8616, 0.8594, 0.8572, 0.8548,
                0.8524, 0.8499, 0.8473, 0.8446, 0.8418, 0.8389, 0.8359, 0.8328, 0.8295, 0.8262, 0.8227, 0.8191,
                0.8155, 0.8119, 0.8083, 0.8048, 0.8013, 0.7979, 0.7945, 0.7912, 0.7879, 0.7846, 0.7813, 0.778,
                0.7747, 0.7714, 0.768, 0.7647, 0.7612, 0.7577, 0.7538, 0.7496, 0.7449, 0.7398, 0.7342, 0.7279, 0.721,
                0.7137, 0.7059, 0.6977, 0.6889, 0.6797, 0.6698, 0.6593, 0.6482, 0.6367, 0.6247, 0.6121, 0.5989,
                0.5852, 0.571, 0.5561, 0.5402, 0.5232, 0.505, 0.4855, 0.4643, 0.4414, 0.4166, 0.3893, 0.3577, 0.3204,
                0.2764, 0.2272, 0.1774, 0.1231, 0.0855, 0.0849};
        Double[] uarray = {0.0824, 0.0704, 0.073, 0.0749, 0.0759, 0.0775,
                0.0803, 0.0836, 0.0869, 0.0902, 0.0936, 0.0972, 0.1009, 0.1043, 0.1075, 0.1104, 0.1131, 0.1156,
                0.118, 0.1203, 0.1224, 0.1245, 0.1265, 0.1285, 0.1304, 0.1323, 0.1342, 0.1363, 0.1384, 0.1406,
                0.1428, 0.1452, 0.1476, 0.1501, 0.1527, 0.1554, 0.1582, 0.1611, 0.1641, 0.1672, 0.1705, 0.1738,
                0.1773, 0.1809, 0.1845, 0.1881, 0.1917, 0.1952, 0.1987, 0.2021, 0.2055, 0.2088, 0.2121, 0.2154,
                0.2187, 0.222, 0.2253, 0.2286, 0.232, 0.2353, 0.2388, 0.2423, 0.2462, 0.2504, 0.2551, 0.2602, 0.2658,
                0.2721, 0.279, 0.2863, 0.2941, 0.3023, 0.3111, 0.3203, 0.3302, 0.3407, 0.3518, 0.3633, 0.3753,
                0.3879, 0.4011, 0.4148, 0.429, 0.4439, 0.4598, 0.4768, 0.495, 0.5145, 0.5357, 0.5586, 0.5834, 0.6107,
                0.6423, 0.6796, 0.7236, 0.7728, 0.8226, 0.8769, 0.9145, 0.9151};
        List<Double> dd = Arrays.asList(rarray);
        for (Double d : dd) {
            urban.addData(d);
        }
        dd = Arrays.asList(uarray);
        for (Double d : dd) {
            rural.addData(d);
        }

        cc.getSeriesList().add(urban);
        cc.getSeriesList().add(rural);
        try {
            HighChart chart = HighChartFactory.renderChart(cc);
            chart.setWidth("400px");
            chart.setHeight("300px");
            System.out.println("PieChart Script : " + cc.getHighChartValue());
            return chart;
        } catch (HighChartsException e) {
            e.printStackTrace();
        }

//        PieChartData melons = new PieChartData("Melons", 3);
//        PieChartData apples = new PieChartData("Apples", 95);
//
//        pieFruits.getData().add(bananas);
//        pieFruits.getData().add(melons);
//        pieFruits.getData().add(apples);
        return null;
    }
}
