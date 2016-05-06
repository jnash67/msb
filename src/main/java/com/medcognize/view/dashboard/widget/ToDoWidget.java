package com.medcognize.view.dashboard.widget;

import com.medcognize.view.dashboard.DashboardView;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;

public abstract class ToDoWidget extends PanelWidget {
    ArrayList<String> toDos = new ArrayList<>();
    TextArea toDoText;

    public ToDoWidget(final String title, final ArrayList<String> initialToDos, final DashboardView view) {
        super(view);
        toDoText = new TextArea(title);
        toDoText.setEnabled(false);
        if (null != initialToDos) {
            toDos = initialToDos;
        }
        setDisplayText();
        toDoText.setCaptionAsHtml(true);
        toDoText.setSizeFull();
        toDoText.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        createContent(toDoText);
        // getContent().setSizeFull();
        // addStyleName("plain");
        // addStyleName("notes");
    }

    public void addToDo(String toDo) {
        toDos.add(toDo);
        setDisplayText();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void removeToDo(String toDo) {
        toDos.remove(toDo);
        setDisplayText();
    }

    public void clearToDos() {
        toDos.clear();
    }

    public void setDisplayText() {
        String full = "";
        for (String toDo : toDos) {
            full = full + "<li>" + toDo + "</li>\n";
        }
        toDoText.setValue(full);
        System.out.println(full);
    }
}