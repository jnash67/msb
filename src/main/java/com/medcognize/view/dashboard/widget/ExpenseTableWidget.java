package com.medcognize.view.dashboard.widget;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.MedicalExpense;
import com.medcognize.domain.User;
import com.medcognize.form.DisplayFriendlyForm;
import com.medcognize.form.MedicalExpenseForm;
import com.medcognize.view.crud.EditTable;
import com.medcognize.view.dashboard.DashboardView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jnash on 6/26/2014.
 */
public class ExpenseTableWidget extends TableWidget {

    private class MedicalExpenseEditTable extends EditTable<MedicalExpense> {

        public MedicalExpenseEditTable(Class<? extends DisplayFriendlyForm<MedicalExpense>> formClazz, ArrayList<String> orderedPidList) {
            super(MedicalExpense.class, formClazz, orderedPidList);
        }

        @Override
        protected void saveItem(BeanItem<MedicalExpense> bi, boolean isNew) {
                collectionOwner.getMedicalExpenses().add(bi.getBean());
        }
    }

    public ExpenseTableWidget(User u, DashboardView view) {
        super("Recent Expenses", view);
        EditTable<MedicalExpense> table = new MedicalExpenseEditTable(MedicalExpenseForm.class, null);

        ArrayList<String> pids = new ArrayList<String>() {
            {
                add("date");
                add("familyMember");
                add("costAccordingToProvider");
            }
        };
        String[] customOrder = new String[]{"date", "familyMember", "costAccordingToProvider"};
        table.setPropertyIds(pids);
        table.setVisibleColumns((Object[]) customOrder);
        table.setData(u.getRepo().getAll(u, MedicalExpense.class), u);
        table.setConverter("date", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(java.util.Locale locale) {
                return new SimpleDateFormat(MedcognizeUI.US_DATE_FORMAT);
            }
        });
        table.setLocale(MedcognizeUI.LOCALE);
        table.setConverter("costAccordingToProvider", new StringToDoubleConverter() {
            @Override
            protected NumberFormat getFormat(java.util.Locale locale) {
                return NumberFormat.getCurrencyInstance(locale);
            }
        });
        setTable(table);
    }
}
