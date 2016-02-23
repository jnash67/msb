package com.medcognize.form.field.errorful;

import com.vaadin.data.Validator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;

import java.util.List;

public interface ErrorfulFieldLayout extends Layout {

    @Override
    public void addComponent(Component c);

    @Override
    public void addComponents(Component... components);

    public List<Field<?>> getFields();

    public List<Field<?>> getExcludedFields();

    public void addExcludedField(Field<?> fieldToExcludeFromHighlighting);

    public Validator.InvalidValueException highlightInvalidFields();

    public void discardInvalidBufferedValues();

    public void setErrorStyleNameForField(Field<?> field, String styleName);

    public String getErrorStyleNameForField(Field<?> field);
}
