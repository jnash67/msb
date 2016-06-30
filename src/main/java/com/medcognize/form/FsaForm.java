package com.medcognize.form;

import com.medcognize.domain.Fsa;
import com.medcognize.domain.Plan;
import com.medcognize.domain.validator.vaadin.ExistingFsaNameValidator;
import com.medcognize.form.field.ViritinFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.addon.daterangefield.DateRangeField;
import org.vaadin.risto.stepper.DateStepper;
import org.vaadin.risto.stepper.IntStepper;

public class FsaForm extends DisplayFriendlyForm<Fsa> {

    Field<?> fsaNameField;

    // we will show the DateRangeField instead of the three other Fields
    DateRangeField dateRangeField;
    IntStepper fsaYearField;
    DateStepper fsaStartDateField;
    DateStepper fsaEndDateField;

    Field<?> amountInFsa;

    public FsaForm(BeanItem<Fsa> bean, boolean isNew) {
        super(bean, null, new ViritinFieldGroupFieldFactory(), isNew);
    }

    @Override
    public void setupForm() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        BeanFieldGroup<Fsa> group = this.getFieldGroup();

        fsaNameField = group.getField("fsaName");
        addComponent(fsaNameField);
        if (isNew()) {
            fsaNameField.addValidator(new ExistingFsaNameValidator(null));
        } else {
            fsaNameField.addValidator(new ExistingFsaNameValidator((String) fsaNameField.getValue()));
        }
        fsaYearField = (IntStepper) group.getField("fsaYear");
        fsaStartDateField = (DateStepper) group.getField("fsaStartDate");
        fsaEndDateField = (DateStepper) group.getField("fsaEndDate");
        amountInFsa = group.getField("amountInFsa");

        addComponent(fsaNameField);

        //noinspection unchecked
        dateRangeField = new DateRangeField(fsaStartDateField.getPropertyDataSource(), fsaEndDateField.getPropertyDataSource(), true,
                fsaYearField.getPropertyDataSource(), true);
        dateRangeField.setErrorStyleName("steppererrorstyle");
        addComponent(dateRangeField);
        addExcludedField(dateRangeField);
        dateRangeField.setMinYear(Plan.MIN_YEAR);
        dateRangeField.setMaxYear(Plan.MAX_YEAR);

        HorizontalLayout dateLayout = new HorizontalLayout();
        dateLayout.setSpacing(true);

        addComponent(amountInFsa);
    }

    @Override
    public void commit() throws FieldGroup.CommitException {
        dateRangeField.commit();
        super.commit();
    }
}