package com.medcognize.view.crud;

import com.medcognize.domain.MedicalExpense;
import com.medcognize.form.DisplayFriendlyForm;
import com.vaadin.data.util.BeanItem;

import java.util.ArrayList;

/**
 * Created by jnash on 2/23/2016.
 */
public class MedicalExpenseTable extends CrudTable<MedicalExpense> {

    public MedicalExpenseTable(final Class<? extends DisplayFriendlyForm<MedicalExpense>> formClazz,
                         ArrayList<String> orderedPidList) {
        super(MedicalExpense.class, formClazz, orderedPidList);
    }

    @Override
    protected void deleteAction(Object target) {
        BeanItem<MedicalExpense> bi = getContainer().getItem(target);
        removeItem(target);
        collectionOwner.getMedicalExpenses().remove(bi.getBean());
        refreshItems();
    }

    @Override
    protected void saveItem(BeanItem<MedicalExpense> bi, boolean isNew) {
        collectionOwner.getMedicalExpenses().add(bi.getBean());
    }
}
