package com.medcognize.view.crud;

import com.medcognize.domain.MedicalExpense;
import com.medcognize.form.DisplayFriendlyForm;

import java.util.ArrayList;

/**
 * Created by jnash on 2/23/2016.
 */
public class MedicalExpenseTable extends CrudTable<MedicalExpense> {

    public MedicalExpenseTable(final Class<? extends DisplayFriendlyForm<MedicalExpense>> formClazz,
                         ArrayList<String> orderedPidList) {
        super(MedicalExpense.class, formClazz, orderedPidList);
    }
}
