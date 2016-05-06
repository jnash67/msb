package com.medcognize.view.dashboard.widget;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.MedicalExpense;
import com.medcognize.domain.Plan;
import com.medcognize.domain.User;
import com.medcognize.util.DChartsUtil;
import com.medcognize.view.dashboard.DashboardView;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.base.elements.XYseries;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.data.Ticks;
import org.dussan.vaadin.dcharts.metadata.TooltipAxes;
import org.dussan.vaadin.dcharts.metadata.XYaxes;
import org.dussan.vaadin.dcharts.metadata.locations.LegendLocations;
import org.dussan.vaadin.dcharts.metadata.locations.TooltipLocations;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.LabelRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.TickRenderers;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.AxesDefaults;
import org.dussan.vaadin.dcharts.options.Highlighter;
import org.dussan.vaadin.dcharts.options.Legend;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.Series;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import org.dussan.vaadin.dcharts.renderers.label.CanvasAxisLabelRenderer;
import org.dussan.vaadin.dcharts.renderers.tick.AxisTickRenderer;
import org.dussan.vaadin.dcharts.renderers.tick.CanvasAxisTickRenderer;

import java.util.ArrayList;
import java.util.List;

/*
For a single Plan which you can't change, this let's you specify different metrics
to see a chart for.  The problem is that two Selects / NativeSelects / ComboBoxes don't seem to work in the PanelWidget.
 */
public class PlanMetricChartWidget extends PanelWidget {

    DCharts chart;
    final String caption;
    final Plan planToChart;
    User user;
    List<MedicalExpense> expensesForPlan;
    List<FamilyMember> familyMembers;
    ComboBox metricSelect;
    VerticalLayout combined = new VerticalLayout();
    Object[] monthsAxis = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    // Object[] monthInitsAxis = {"J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"};

    static final ArrayList<String> metrics = new ArrayList<String>() {
        {
            add("Costs");
            add("Out of Pocket");
            add("Deductibles");
        }
    };

    public PlanMetricChartWidget(final User u, final Plan p, final String caption, final DashboardView view) {
        super(view);
        this.caption = caption;
        this.planToChart = p;
        this.user = u;
        expensesForPlan = u.getRepo().getMedicalExpensesForPlan(u, this.planToChart);
        familyMembers = u.getRepo().getFamilyMembersWithPlanExpenses(u, this.planToChart);

        metricSelect = new ComboBox("", metrics);
        metricSelect.select(metrics.get(0));
        metricSelect.setImmediate(true);
        metricSelect.setNullSelectionAllowed(false);
        metricSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                createLayout();
            }
        });
        metricSelect.setTextInputAllowed(false);

        combined.setCaption(caption);
        combined.setSpacing(true);

        createLayout();
        createContent(combined);
    }

    private void createChart() {
        DataSeries dataSeries = new DataSeries();
        for (FamilyMember fm : familyMembers) {
            ArrayList<Double> familyMemberExpenses = DChartsUtil.getMedicalExpensesForFamilyMemberByMonth(fm,
                    expensesForPlan);
            // System.out.println("Series for " + fm.getFamilyMemberName() + ": " + familyMemberExpenses);
            dataSeries.add(familyMemberExpenses.toArray());
        }

        SeriesDefaults seriesDefaults = new SeriesDefaults().setFillToZero(true).setRenderer(SeriesRenderers.BAR);
        AxesDefaults axesDefaults = new AxesDefaults().setLabelRenderer(LabelRenderers.CANVAS);

        Series series = new Series();
        for (FamilyMember fm : familyMembers) {
            series.addSeries(new XYseries().setLabel(fm.getFamilyMemberName()));
        }

        // Highlighting tooltips don't work with LegendPlacements.OUTSIDE_GRID
        Legend legend = new Legend().setLocation(LegendLocations.NORTH_EAST).setShow(true);

        XYaxis xAxis = new XYaxis(XYaxes.X).setRenderer(AxisRenderers.CATEGORY).setTicks(new Ticks().add(monthsAxis));
        xAxis.setTickRenderer(TickRenderers.CANVAS).setTickOptions(new CanvasAxisTickRenderer().setAngle(-90));
        xAxis.setLabel(String.valueOf(this.planToChart.getPlanYear()));
        xAxis.setLabelOptions(new CanvasAxisLabelRenderer().setFontSize("10pt"));

        XYaxis yAxis = new XYaxis(XYaxes.Y).setTickOptions(new AxisTickRenderer().setFormatString("$%d"));
        yAxis.setMin(0);

        Axes axes = new Axes().addAxis(xAxis).addAxis(yAxis);

        Highlighter highlighter = new Highlighter().setShow(true).setShowTooltip(true).setTooltipAlwaysVisible(true)
                .setKeepTooltipInsideChart(true).setTooltipLocation(TooltipLocations.NORTH).setTooltipAxes
                        (TooltipAxes.XY_BAR);

        Options options = new Options();
        options.addOption(seriesDefaults);
        options.addOption(axesDefaults);
        options.addOption(series);
        options.addOption(axes);
        options.addOption(legend);
        options.addOption(highlighter);

        chart = new DCharts().setDataSeries(dataSeries).setOptions(options).show();
    }

    private void createLayout() {
        combined.removeAllComponents();
        createChart();
        combined.addComponent(metricSelect);
        combined.addComponent(chart);

        if (familyMembers.size() == 0) {
            Label l = new Label("Plan has no expenses by any family member");
            combined.addComponent(l);
            combined.setWidth("400px");
            combined.setHeight("190px");
        } else {
            combined.addComponent(chart);
            chart.setWidth("400px");
            chart.setHeight("190px");
        }
    }

    public DCharts getChart() {
        return chart;
    }
}
