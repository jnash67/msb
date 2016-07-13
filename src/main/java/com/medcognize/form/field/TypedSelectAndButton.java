package com.medcognize.form.field;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.layouts.MHorizontalLayout;

public class TypedSelectAndButton<T> extends TypedSelect<T> {

    Button actionButton = null;

    public TypedSelectAndButton() {
    }

    public TypedSelectAndButton(Class<T> type) {
        super(type);
    }

    public TypedSelectAndButton(Class<T> type, Button actionButton) {
        super(type);
        setButton(actionButton);
    }

    public TypedSelectAndButton<T> withButton(MButton actionButton) {
        setButton(actionButton);
        return this;
    }

    public void setButton(Button actionButton) {
        this.actionButton = actionButton;
    }

    @Override
    protected Component initContent() {
        if (null != actionButton) {
            return new MHorizontalLayout(super.initContent(), actionButton).withMargin(false);
        }
        return super.initContent();
    }
}
