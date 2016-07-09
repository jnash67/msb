package com.medcognize.form;

import com.medcognize.domain.Fsa;
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

    public FsaForm(Fsa item) {
        super(Fsa.class, null);
        setSizeUndefined();
        setEntity(item);
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(new MFormLayout(fsaName, fsaYear, fsaStartDate, fsaEndDate, amountInFsa), getToolbar());
    }

}