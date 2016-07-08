package com.medcognize.view.crud;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.MedicalExpense;
import com.medcognize.domain.User;
import com.medcognize.form.MedicalExpenseForm;
import com.medcognize.form.wizard.MedicalExpenseWizard;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@SpringView(name = MedicalExpenseView.NAME)
public class MedicalExpenseView extends CrudView<MedicalExpense> {
    public static final String NAME = "expense";
    static final ArrayList<String> pids = new ArrayList<String>() {
        {
            add("date");
            add("plan");
            add("familyMember");
            add("provider");
            add("medicalExpenseType");
            add("outOfPocketAmount");
            add("costAccordingToProvider");
        }
    };

    public MedicalExpenseView() {
        super(MedicalExpense.class, "Medical Expenses", new MedicalExpenseTable(MedicalExpenseForm.class, pids));
        User u = DbUtil.getLoggedInUser();
        if (null == u) {
            log.error("owner should not be null here");
            return;
        }
        Collection<MedicalExpense> expenses = UserUtil.getAll(u, MedicalExpense.class);
        setData(expenses, u);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
        table.sortTable("date");
        table.setColumnWidth("familyMember", 65);
        table.setColumnWidth("outOfPocketAmount", 65);
        table.setColumnWidth("costAccordingToProvider", 65);
        table.addStyleName("wordwrap-headers");
        table.setConverter("date", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(java.util.Locale locale) {
                return new SimpleDateFormat(MedcognizeUI.US_DATE_FORMAT);
            }
        });
        table.setConverter("outOfPocketAmount", new StringToDoubleConverter() {
            @Override
            protected NumberFormat getFormat(java.util.Locale locale) {
                return NumberFormat.getCurrencyInstance(locale);
            }
        });
        table.setConverter("costAccordingToProvider", new StringToDoubleConverter() {
            @Override
            protected NumberFormat getFormat(java.util.Locale locale) {
                return NumberFormat.getCurrencyInstance(locale);
            }
        });
        if (showHeader) {
            Button ezAdd = new Button("EZ Add");
            ezAdd.setDescription("Add an expense using the easy wizard");
            ezAdd.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Window w = getTable().addAction(MedicalExpenseWizard.class);
                    if (null != w) {
                        w.setSizeUndefined();
                    }
                    //w.setWidth("600px");
                }
            });
            ezAdd.addStyleName("small");
            toolbar.addComponent(ezAdd);
            toolbar.setComponentAlignment(ezAdd, Alignment.MIDDLE_LEFT);
            toolbar.showAddNewButton();
        }
    }
}
