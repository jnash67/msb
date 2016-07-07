package com.medcognize.form;

import com.medcognize.domain.Fsa;
import com.medcognize.domain.Plan;
import com.medcognize.domain.validator.vaadin.ExistingFsaNameValidator;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import org.vaadin.addon.daterangefield.DateRangeField;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collection;

public class FsaForm extends DisplayFriendlyForm<Fsa> {

    Field<?> fsaNameField;

    // we will show the DateRangeField instead of the three other Fields. Changes to DateRangeField
    // will be propagated to the properties of the sub-fields.
    DateRangeField dateRangeField;
    Field<?>  fsaYearField;
    Field<?>  fsaStartDateField;
    Field<?>  fsaEndDateField;

    Field<?> amountInFsaField;

    public FsaForm(Fsa item) {
        super(item, null, null);
    }

    public FsaForm(Fsa item, Collection<String> pids) {
        super(item, pids, null);
    }


    @Override
    protected Component createContent() {
        fsaNameField = group.getField("fsaName");
        fsaYearField = group.getField("fsaYear");
        fsaStartDateField = group.getField("fsaStartDate");
        fsaEndDateField = group.getField("fsaEndDate");
        amountInFsaField = group.getField("amountInFsaField");

        ((AbstractField)amountInFsaField).setConverter(new StringToDoubleConverter());
        fsaNameField.addValidator(new ExistingFsaNameValidator((String) fsaNameField.getValue()));
        dateRangeField = new DateRangeField(fsaStartDateField.getPropertyDataSource(), fsaEndDateField.getPropertyDataSource(), true,
                fsaYearField.getPropertyDataSource(), true);
        dateRangeField.setMinYear(Plan.MIN_YEAR);
        dateRangeField.setMaxYear(Plan.MAX_YEAR);

        return new MVerticalLayout(
                getToolbar(),
                new FormLayout(fsaNameField, dateRangeField, amountInFsaField));
    }

}