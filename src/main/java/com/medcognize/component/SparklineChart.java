package com.medcognize.component;

import com.medcognize.data.dummy.DummyDataGenerator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.ArrayUtils;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.data.DataSeries;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class SparklineChart extends VerticalLayout {

    public SparklineChart(final String name, final String unit,
            final String prefix, final String color, final int howManyPoints,
            final int min, final int max) {
        setSizeUndefined();
        addStyleName("spark");
        setDefaultComponentAlignment(Alignment.TOP_CENTER);

        int[] values = DummyDataGenerator.randomSparklineValues(howManyPoints,
                min, max);

        Label current = new Label(prefix + values[values.length - 1] + unit);
        current.setSizeUndefined();
        current.addStyleName(ValoTheme.LABEL_HUGE);
        addComponent(current);

        Label title = new Label(name);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_SMALL);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        addComponent(title);

        addComponent(buildSparkline(values, color));

        List<Integer> vals = Arrays.asList(ArrayUtils.toObject(values));
        Label highLow = new Label("High <b>" + java.util.Collections.max(vals)
                + "</b> &nbsp;&nbsp;&nbsp; Low <b>"
                + java.util.Collections.min(vals) + "</b>", ContentMode.HTML);
        highLow.addStyleName(ValoTheme.LABEL_TINY);
        highLow.addStyleName(ValoTheme.LABEL_LIGHT);
        highLow.setSizeUndefined();
        addComponent(highLow);

    }

    private Component buildSparkline(final int[] values, final String color) {
        DCharts spark = new DCharts();
        // spark.getOptions().setTitle("");
        spark.setWidth("120px");
        spark.setHeight("40px");

        DataSeries series = new DataSeries();
        DataSeries items= series.newSeries();
        for (int i = 0; i < values.length; i++) {
            items.add("", values[i]);
        }
        spark.setDataSeries(series);

        return spark;
    }
}
