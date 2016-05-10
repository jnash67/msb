package com.medcognize.view.dashboard.widget;

import com.medcognize.view.dashboard.FlotChart;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

public class FlotChartWidget extends ContentWidget {

    FlotChart flot ;
    final String caption;

    public FlotChartWidget(final String caption, VerticalLayout root, CssLayout dashboardPanels) {
        super(root, dashboardPanels);
        this.caption = caption;
        String data = "[" +
                "[" +
                "[0, 5]," +
                "[2, 7]," +
                "[4, 8]," +
                "[10, 5]" +
                "]" +
                "]";
        String options = "{" +
                "grid:{" +
                "backgroundColor:{" +
                "colors:[" +
                "\"#fef\"," +
                "\"#eee\"" +
                "]" +
                "}" +
                "}" +
                "}";
	    flot= new FlotChart(caption, data, options);
	    flot.setWidth("300px");
	    flot.setHeight("300px");
        // flot.addStyleName("plain");
        // flot.addStyleName("borderless");
        setContent(flot);
    }
}
