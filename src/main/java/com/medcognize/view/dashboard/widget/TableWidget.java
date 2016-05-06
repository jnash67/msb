package com.medcognize.view.dashboard.widget;

import com.medcognize.view.dashboard.DashboardView;
import com.vaadin.ui.Table;

public class TableWidget extends PanelWidget {

    Table t;
    final String caption;

    public TableWidget(final String caption, final DashboardView view) {
        super(view);
        this.caption = caption;
    }

    public void setTable(Table table) {
        this.t = table;
        t.setCaption(caption);
	    t.setSizeFull();
        //t.setWidth("100%");
        t.setPageLength(0);
        t.addStyleName("plain");
        t.addStyleName("borderless");
        t.setSortEnabled(false);
        t.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        createContent(t);
    }

    public Table getTable() {
        return t;
    }

}
