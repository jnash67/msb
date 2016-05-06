package com.medcognize.view.dashboard.widget;

import com.medcognize.view.dashboard.DashboardView;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;

public class NotesWidget extends PanelWidget {

    public NotesWidget(final String title, final String msg, final DashboardView view) {
        super(view);
        TextArea notes = new TextArea(title);
        notes.setValue(msg);
        notes.setSizeFull();
        notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        createContent(notes);

        //getContent().setSizeFull();
        addStyleName("notes");
    }
}
