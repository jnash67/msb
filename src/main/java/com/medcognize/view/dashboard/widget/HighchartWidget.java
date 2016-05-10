package com.medcognize.view.dashboard.widget;

import com.medcognize.view.dashboard.Highcharts;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

public class HighchartWidget extends ContentWidget {

	Highcharts highchartsPie;
    final String caption;

    public HighchartWidget(final String caption, VerticalLayout root, CssLayout dashboardPanels) {
        super(root, dashboardPanels);
        this.caption = caption;

        String jsonData =
                "{" +
                        "chart : " +
                        "{renderTo : 'chart',}, " +
                        "series : " +
                        "[ " +
                        "{" +
                        "type : 'pie', " +
                        "data : " +
                        "[ " +
                        "[ 'Im average looking.', 2 ], " +
                        "[ 'Im shy around girls.', 3 ], " +
                        "[ 'Im level 80 Paladin.', 95 ] " +
                        "] " +
                        "} " +
                        "] " +
                        "}";

	    highchartsPie = new Highcharts();
	    highchartsPie.setData(jsonData);
	    highchartsPie.setId("chart");
	    highchartsPie.setCaption(caption);

	    highchartsPie.setWidth("400px");
        highchartsPie.setHeight("300px");
      //  highchartsPie.addStyleName("plain");
       // highchartsPie.addStyleName("borderless");
        setContent(highchartsPie);
    }
}
