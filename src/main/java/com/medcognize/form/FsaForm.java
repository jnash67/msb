package com.medcognize.form;

import com.medcognize.domain.Fsa;
import com.medcognize.domain.validator.vaadin.ExistingFsaNameValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class FsaForm extends DisplayFriendlyForm<Fsa> {

    Field<?> fsaName = createField("fsaName");
    Field<?> fsaYear = createField("fsaYear");
    Field<?> fsaStartDate = createField("fsaStartDate");
    Field<?> fsaEndDate = createField("fsaEndDate");
    Field<?> amountInFsa = createField("amountInFsa");

    public FsaForm(Fsa item, boolean isNew) {
        super(Fsa.class, isNew, null);
        // setSizeUndefined();
        setEntity(item);
        if (addingNewItem()) {
            fsaName.addValidator(new ExistingFsaNameValidator((String) fsaName.getValue()));
        } else {
            fsaName.setEnabled(false);
        }
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(new MFormLayout(fsaName, fsaYear, fsaStartDate, fsaEndDate, amountInFsa), getToolbar());
    }

}