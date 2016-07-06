package com.medcognize.form;

import com.medcognize.domain.Fsa;
import com.medcognize.domain.Plan;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.validator.vaadin.ExistingFsaNameValidator;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.vaadin.addon.daterangefield.DateRangeField;
import org.vaadin.viritin.fields.IntegerField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class FsaForm extends AbstractForm<Fsa> {

    TextField fsaNameField = new MTextField(getCaption("fsaNameField"));

    // we will show the DateRangeField instead of the three other Fields
    DateRangeField dateRangeField;
    IntegerField fsaYearField = new IntegerField(getCaption("fsaYearField"));
    DateField fsaStartDateField = new DateField(getCaption("fsaStartDateField"));
    DateField fsaEndDateField = new DateField(getCaption("fsaEndDateField"));
    TextField amountInFsa = new MTextField("amountInFsa");

    @Override
    protected Component createContent() {
        amountInFsa.setConverter(new StringToDoubleConverter());
        dateRangeField = new DateRangeField(fsaStartDateField.getPropertyDataSource(), fsaEndDateField.getPropertyDataSource(), true,
                fsaYearField.getPropertyDataSource(), true);
        dateRangeField.setErrorStyleName("steppererrorstyle");
        dateRangeField.setMinYear(Plan.MIN_YEAR);
        dateRangeField.setMaxYear(Plan.MAX_YEAR);
        fsaNameField.addValidator(new ExistingFsaNameValidator((String) fsaNameField.getValue()));

        return new MVerticalLayout(
                getToolbar(),
                new FormLayout(fsaNameField, dateRangeField, amountInFsa));
    }

    private String getCaption(String propName) {
        return DisplayFriendly.getPropertyCaption(Fsa.class, propName);
    }

}