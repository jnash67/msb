package com.medcognize.view.dashboard.widget;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.MedicalExpense;
import com.medcognize.domain.Plan;
import com.medcognize.domain.User;
import com.medcognize.util.UserUtil;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
For a single metric which you can't change, this let's you see that metric (i.e. costs)
for various Plans. The problem is that two Selects / NativeSelects / ComboBoxes don't seem to work in the ContentWidget.
 */
public class PlanCostsChartWidget extends ContentWidget {

    // DCharts chart;
    final String caption;
    Plan planToChart;
    User user;
    List<MedicalExpense> expensesForPlan;
    List<FamilyMember> familyMembers;
    final List<Plan> plans;
    Calendar cal = Calendar.getInstance();
    ComboBox planSelect;
    VerticalLayout combined = new VerticalLayout();
    Object[] monthsAxis = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    // Object[] monthInitsAxis = {"J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"};

    public PlanCostsChartWidget(final User u, final String caption, final VerticalLayout root, final CssLayout dashboardPanels) {
        super(root, dashboardPanels);
        this.caption = caption;
        this.planToChart = UserUtil.getActivePlan(u);
        this.user = u;
        this.plans = UserUtil.getAllFromUser(u, Plan.class);
        expensesForPlan = UserUtil.getMedicalExpensesForPlan(u, this.planToChart);
        familyMembers = UserUtil.getFamilyMembersWithPlanExpenses(u, this.planToChart);

        if (0 == this.plans.size()) {
            planSelect = new ComboBox("");
            planSelect.setEnabled(false);
        } else {
            planSelect = new ComboBox("", this.plans);
            planSelect.select(this.planToChart);
            planSelect.setEnabled(true);
            planSelect.setImmediate(true);
            if (!this.plans.contains(null)) {
                planSelect.setNullSelectionAllowed(false);
            }
            planSelect.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    PlanCostsChartWidget.this.planToChart = (Plan) planSelect.getValue();
                    expensesForPlan = UserUtil.getMedicalExpensesForPlan(u, PlanCostsChartWidget.this.planToChart);
                    familyMembers = UserUtil.getFamilyMembersWithPlanExpenses(u, PlanCostsChartWidget.this.planToChart);
                    createLayout();
                }
            });
        }
        planSelect.setTextInputAllowed(false);
        combined.setCaption(caption);
        combined.setSpacing(true);

        createLayout();
        setContent(combined);
    }

    private void createChart() {
//        DataSeries dataSeries = new DataSeries();
//        for (FamilyMember fm : familyMembers) {
//            ArrayList<Double> familyMemberExpenses = DChartsUtil.getMedicalExpensesForFamilyMemberByMonth(fm,
//                    expensesForPlan);
//            // System.out.println("Series for " + fm.getFamilyMemberName() + ": " + familyMemberExpenses);
//            dataSeries.add(familyMemberExpenses.toArray());
//        }
//
//        SeriesDefaults seriesDefaults = new SeriesDefaults().setFillToZero(true).setRenderer(SeriesRenderers.BAR);
//        AxesDefaults axesDefaults = new AxesDefaults().setLabelRenderer(LabelRenderers.CANVAS);
//
//        Series series = new Series();
//        for (FamilyMember fm : familyMembers) {
//            series.addSeries(new XYseries().setLabel(fm.getFamilyMemberName()));
//        }
//
//        // Highlighting tooltips don't work with LegendPlacements.OUTSIDE_GRID
//        // Event handling doesn't work when you use EnhancedLegendRenderer
//        Legend legend = new Legend().setLocation(LegendLocations.NORTH_EAST).setShow(true);
//
//        XYaxis xAxis = new XYaxis(XYaxes.X).setRenderer(AxisRenderers.CATEGORY).setTicks(new Ticks().add(monthsAxis));
//        xAxis.setTickRenderer(TickRenderers.CANVAS).setTickOptions(new CanvasAxisTickRenderer().setAngle(-90));
//        xAxis.setLabel(String.valueOf(this.planToChart.getPlanYear()));
//        xAxis.setLabelOptions(new CanvasAxisLabelRenderer().setFontSize("10pt"));
//
//        XYaxis yAxis = new XYaxis(XYaxes.Y).setTickOptions(new AxisTickRenderer().setFormatString("$%d"));
//        yAxis.setMin(0);
//
//        Axes axes = new Axes().addAxis(xAxis).addAxis(yAxis);
//
//        Highlighter highlighter = new Highlighter().setShow(true).setShowTooltip(true).setTooltipAlwaysVisible(true)
//                .setKeepTooltipInsideChart(true).setTooltipLocation(TooltipLocations.NORTH).setTooltipAxes
//                        (TooltipAxes.XY_BAR);
//
//        Options options = new Options();
//        options.addOption(seriesDefaults);
//        options.addOption(axesDefaults);
//        options.addOption(series);
//        options.addOption(axes);
//        options.addOption(legend);
//        options.addOption(highlighter);
//
//        chart = new DCharts();
//        chart.setDataSeries(dataSeries).setOptions(options).show();
//        chart.setWidth("400px");
//        chart.setHeight("200px");
    }

    private void createLayout() {
        combined.removeAllComponents();
        createChart();
        combined.addComponent(planSelect);

        if (familyMembers.size() == 0) {
            Label l = new Label("Plan has no expenses by any family member");
            combined.addComponent(l);
        } else {
            // combined.addComponent(chart);
        }
    }

//    public DCharts getChart() {
//        return chart;
//    }

}
