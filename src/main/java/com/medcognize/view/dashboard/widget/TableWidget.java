package com.medcognize.view.dashboard.widget;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by jnash on 5/9/2016.
 */
public class TableWidget extends ContentWidget {

    Table t;
    final String caption;

    public TableWidget(final String caption, final VerticalLayout root, final CssLayout dashboardPanels) {
        super(root, dashboardPanels);
        this.caption = caption;
    }

    public void setTable(Table table) {
        this.t = table;
        t.setCaption(caption);
//        t.addStyleName(ValoTheme.TABLE_BORDERLESS);
//        t.addStyleName(ValoTheme.TABLE_NO_STRIPES);
//        t.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
//        t.addStyleName(ValoTheme.TABLE_SMALL);
        t.addStyleName("plain");
        t.addStyleName("borderless");
        t.setSortEnabled(false);
        t.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        // t.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        t.setSizeFull();
        setContent(t);
    }

    public Table getTable() {
        return t;
    }
}
