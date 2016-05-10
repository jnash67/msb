package com.medcognize.view.admin;

import com.medcognize.domain.User;
import com.medcognize.util.DChartsUtil;
import com.medcognize.util.DbUtil;
import com.medcognize.view.dashboard.widget.PlanCostsChartWidget;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.PointLabels;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.data.Ticks;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickEvent;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickHandler;
import org.dussan.vaadin.dcharts.events.mouseenter.ChartDataMouseEnterEvent;
import org.dussan.vaadin.dcharts.events.mouseenter.ChartDataMouseEnterHandler;
import org.dussan.vaadin.dcharts.events.mouseleave.ChartDataMouseLeaveEvent;
import org.dussan.vaadin.dcharts.events.mouseleave.ChartDataMouseLeaveHandler;
import org.dussan.vaadin.dcharts.events.rightclick.ChartDataRightClickEvent;
import org.dussan.vaadin.dcharts.events.rightclick.ChartDataRightClickHandler;
import org.dussan.vaadin.dcharts.metadata.LegendPlacements;
import org.dussan.vaadin.dcharts.metadata.XYaxes;
import org.dussan.vaadin.dcharts.metadata.directions.BarDirections;
import org.dussan.vaadin.dcharts.metadata.locations.LegendLocations;
import org.dussan.vaadin.dcharts.metadata.locations.PointLabelLocations;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.LabelRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.LegendRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.metadata.ticks.TickFormatters;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.Grid;
import org.dussan.vaadin.dcharts.options.Highlighter;
import org.dussan.vaadin.dcharts.options.Legend;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import org.dussan.vaadin.dcharts.options.Title;
import org.dussan.vaadin.dcharts.renderers.legend.EnhancedLegendRenderer;
import org.dussan.vaadin.dcharts.renderers.series.BarRenderer;
import org.dussan.vaadin.dcharts.renderers.tick.CanvasAxisTickRenderer;

public class ChartTestTab extends Panel {

    public ChartTestTab() {
        super();
        User u = DbUtil.getLoggedInUser();
        //FlotChartWidget cw = new FlotChartWidget("Chart");
        //HighchartWidget hw = new HighchartWidget("Pie");
        PlanCostsChartWidget  dw = new PlanCostsChartWidget(u, "Test", null, null);
        DCharts chart = createChart2();
        DCharts chart2 = dw.getChart();
        DCharts chart3 = createChart3();

        //QuadrantWidget qw = new QuadrantWidget("Charts", cw, hw, dw, null);
        VerticalLayout vl = new VerticalLayout();
        vl.setSpacing(true);
        vl.setMargin(true);
        vl.addComponent(chart);
        vl.addComponent(chart2);
        vl.addComponent(chart3);
        setContent(vl);
    }

    public DCharts createChart3() {
        DataSeries dataSeries = new DataSeries()
                .add(2, 6, 7, 10);

        SeriesDefaults seriesDefaults = new SeriesDefaults()
                .setRenderer(SeriesRenderers.BAR);

        Axes axes = new Axes()
                .addAxis(
                        new XYaxis()
                                .setRenderer(AxisRenderers.CATEGORY)
                                .setTicks(
                                        new Ticks()
                                                .add("a", "b", "c", "d"))) ;

        Highlighter highlighter = new Highlighter()
                .setShow(false);

        Options options = new Options()
                .setCaptureRightClick(true)
                .setSeriesDefaults(seriesDefaults)
                .setAxes(axes)
                .setHighlighter(highlighter);

        DCharts chart = new DCharts();

        chart.setEnableChartDataMouseEnterEvent(true);
        chart.setEnableChartDataMouseLeaveEvent(true);
        chart.setEnableChartDataClickEvent(true);
        chart.setEnableChartDataRightClickEvent(true);


        chart.addHandler(new ChartDataMouseEnterHandler() {
            @Override
            public void onChartDataMouseEnter(ChartDataMouseEnterEvent event) {
                Notification.show("CHART DATA MOUSE ENTER", DChartsUtil.getChartDataMouseEnterEventString(event),
                        Notification.Type.HUMANIZED_MESSAGE);
            }
        });

        chart.addHandler(new ChartDataMouseLeaveHandler() {
            @Override
            public void onChartDataMouseLeave(ChartDataMouseLeaveEvent event) {
                Notification.show("CHART DATA MOUSE LEAVE", event.getChartData().toString(),
                        Notification.Type.HUMANIZED_MESSAGE);
            }
        });

        chart.addHandler(new ChartDataClickHandler() {
            @Override
            public void onChartDataClick(ChartDataClickEvent event) {
                Notification.show("CHART DATA CLICK", DChartsUtil.getChartDataClickEventString(event),
                        Notification.Type.HUMANIZED_MESSAGE);
            }
        });

        chart.addHandler(new ChartDataRightClickHandler() {
            @Override
            public void onChartDataRightClick(ChartDataRightClickEvent event) {
                Notification.show("CHART DATA RIGHT CLICK", event.getChartData().toString(),
                        Notification.Type.HUMANIZED_MESSAGE);
            }
        });

        chart.setDataSeries(dataSeries)
                .setOptions(options)
                .show();

        return chart;
    }

    public DCharts createChart2() {
        DataSeries dataSeries = new DataSeries().add(0.9176, 0.9296, 0.927, 0.9251, 0.9241, 0.9225, 0.9197, 0.9164,
                0.9131, 0.9098, 0.9064, 0.9028, 0.8991, 0.8957, 0.8925, 0.8896, 0.8869, 0.8844, 0.882, 0.8797,
                0.8776, 0.8755, 0.8735, 0.8715, 0.8696, 0.8677, 0.8658, 0.8637, 0.8616, 0.8594, 0.8572, 0.8548,
                0.8524, 0.8499, 0.8473, 0.8446, 0.8418, 0.8389, 0.8359, 0.8328, 0.8295, 0.8262, 0.8227, 0.8191,
                0.8155, 0.8119, 0.8083, 0.8048, 0.8013, 0.7979, 0.7945, 0.7912, 0.7879, 0.7846, 0.7813, 0.778,
                0.7747, 0.7714, 0.768, 0.7647, 0.7612, 0.7577, 0.7538, 0.7496, 0.7449, 0.7398, 0.7342, 0.7279, 0.721,
                0.7137, 0.7059, 0.6977, 0.6889, 0.6797, 0.6698, 0.6593, 0.6482, 0.6367, 0.6247, 0.6121, 0.5989,
                0.5852, 0.571, 0.5561, 0.5402, 0.5232, 0.505, 0.4855, 0.4643, 0.4414, 0.4166, 0.3893, 0.3577, 0.3204,
                0.2764, 0.2272, 0.1774, 0.1231, 0.0855, 0.0849).add(0.0824, 0.0704, 0.073, 0.0749, 0.0759, 0.0775,
                0.0803, 0.0836, 0.0869, 0.0902, 0.0936, 0.0972, 0.1009, 0.1043, 0.1075, 0.1104, 0.1131, 0.1156,
                0.118, 0.1203, 0.1224, 0.1245, 0.1265, 0.1285, 0.1304, 0.1323, 0.1342, 0.1363, 0.1384, 0.1406,
                0.1428, 0.1452, 0.1476, 0.1501, 0.1527, 0.1554, 0.1582, 0.1611, 0.1641, 0.1672, 0.1705, 0.1738,
                0.1773, 0.1809, 0.1845, 0.1881, 0.1917, 0.1952, 0.1987, 0.2021, 0.2055, 0.2088, 0.2121, 0.2154,
                0.2187, 0.222, 0.2253, 0.2286, 0.232, 0.2353, 0.2388, 0.2423, 0.2462, 0.2504, 0.2551, 0.2602, 0.2658,
                0.2721, 0.279, 0.2863, 0.2941, 0.3023, 0.3111, 0.3203, 0.3302, 0.3407, 0.3518, 0.3633, 0.3753,
                0.3879, 0.4011, 0.4148, 0.429, 0.4439, 0.4598, 0.4768, 0.495, 0.5145, 0.5357, 0.5586, 0.5834, 0.6107,
                0.6423, 0.6796, 0.7236, 0.7728, 0.8226, 0.8769, 0.9145, 0.9151);

        Title title = new Title("Contribution of Urban and Rural Population to National Percentiles (edited data)");

        SeriesDefaults seriesDefaults = new SeriesDefaults().
                setShowMarker(false).
                setFill(true).
                setFillAndStroke(true);

        Legend legend = new Legend().
                setShow(true).
                setRenderer(LegendRenderers.ENHANCED).
                setRendererOptions(new EnhancedLegendRenderer().setNumberRows(1)).
                setPlacement(LegendPlacements.OUTSIDE_GRID).
                setLabels("Rural", "Urban").
                setLocation(LegendLocations.SOUTH);

        Axes axes = new Axes().addAxis(new XYaxis(XYaxes.X).
                        setPad(0).
                        setMin(1).
                        setMax(100).
                        setLabel("Population Percentile").
                        setLabelRenderer(LabelRenderers.CANVAS).
                        setTickInterval(3).
                        setTickOptions(new CanvasAxisTickRenderer().setShowGridline(false))).addAxis(new XYaxis
                (XYaxes.Y).
                        setMin(0).
                        setMax(1).
                        setLabel("Percentage of Population").
                        setLabelRenderer(LabelRenderers.CANVAS).
                        setTickOptions(new CanvasAxisTickRenderer().setFormatter(TickFormatters.PERCENT)
                                .setShowGridline(false).setFormatString("%d%%")));

        Grid grid = new Grid().
                setDrawBorder(false).
                setShadow(false).
                setBackground("white");

        Options options = new Options().setTitle(title).setStackSeries(true).setSeriesColors("#77933C",
                "#B9CDE5").setSeriesDefaults(seriesDefaults).setLegend(legend).setAxes(axes).setGrid(grid);

        DCharts chart = new DCharts().setDataSeries(dataSeries).setOptions(options).show();
        return chart;
    }

    public DCharts createChart() {
        DataSeries dataSeries = new DataSeries();
        DataSeries s1 = dataSeries.newSeries();
        s1.add(2, 1).add(4, 2).add(6, 3).add(3, 4);
        DataSeries s2 = dataSeries.newSeries();
        s2.add(5, 1).add(1, 2).add(3, 3).add(4, 4);
        DataSeries s3 = dataSeries.newSeries();
        s3.add(4, 1).add(7, 2).add(1, 3).add(2, 4);

        SeriesDefaults seriesDefaults = new SeriesDefaults().setRenderer(SeriesRenderers.BAR).setPointLabels(new
                PointLabels().setShow(true).setLocation(PointLabelLocations.EAST).setEdgeTolerance(-15))
                .setShadowAngle(135).setRendererOptions(new BarRenderer().setBarDirection(BarDirections.VERTICAL));

        Axes axes = new Axes().addAxis(new XYaxis(XYaxes.Y).setRenderer(AxisRenderers.CATEGORY));

        Options options = new Options().setSeriesDefaults(seriesDefaults).setAxes(axes);

        return new DCharts().setDataSeries(dataSeries).setOptions(options).show();
    }
}
