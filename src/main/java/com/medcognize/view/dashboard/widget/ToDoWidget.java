package com.medcognize.view.dashboard.widget;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;

/**
 * Created by jnash on 5/9/2016.
 */
public class ToDoWidget extends ContentWidget {

    ArrayList<String> toDos= new ArrayList<>();
    Label toDoText;

    public ToDoWidget(String title, ArrayList<String> initialToDos, VerticalLayout root, CssLayout dashboardPanels) {
        super(root, dashboardPanels);
        toDoText = new Label();
        toDoText.setCaption(title);
        toDoText.setContentMode(ContentMode.HTML);
        if (null != initialToDos) {
            toDos = initialToDos;
        }
        setDisplayText();
        toDoText.setSizeFull();
        setContent(toDoText);
        getContent().setSizeFull();
        addStyleName("plain");
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

    public void setDisplayText() {
        String full = "";
        for (String toDo: toDos) {
            full = full + "<li>" + toDo + "</li>\n";
        }
        toDoText.setValue(full);
    }
}
