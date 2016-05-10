package com.medcognize.view.dashboard.widget;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by jnash on 5/9/2016.
 */
public class NotesWidget extends ContentWidget {

    public NotesWidget(String title, String msg, VerticalLayout root, CssLayout dashboardPanels) {
        super(root, dashboardPanels);
        TextArea notes = new TextArea(title);
        notes.setValue(msg);
        notes.setSizeFull();
        notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        setContent(notes);
        addStyleName("notes");
    }
}
