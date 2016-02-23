package com.medcognize.form.field.errorful;

import com.medcognize.util.ErrorfulUtil;
import com.vaadin.data.Validator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorfulHorizontalLayout extends HorizontalLayout implements ErrorfulFieldLayout {

    private List<Field<?>> fieldList = new ArrayList<>();
    private List<Field<?>> excludedFieldList = new ArrayList<>();
    private Map<Field<?>, String> errorStyleNameMap = new HashMap<>();

    @Override
    public void addComponent(Component c) {
        ErrorfulUtil.addErrorChangeListener(c, this);
        super.addComponent(c);
    }

    @Override
    public void addComponents(Component... components) {
        for (Component c : components) {
            addComponent(c);
        }
    }

    @Override
    public List<Field<?>> getFields() {
        return fieldList;
    }

    @Override
    public List<Field<?>> getExcludedFields() {
        return excludedFieldList;
    }

    @Override
    public void addExcludedField(Field<?> fieldToExcludeFromHighlighting) {
        excludedFieldList.add(fieldToExcludeFromHighlighting);
    }

    @Override
    public Validator.InvalidValueException highlightInvalidFields() {
        return ErrorfulUtil.highlightInvalidFields(getFields(), this);
    }

    @Override
    public void discardInvalidBufferedValues() {
        ErrorfulUtil.discardInvalidBufferedValues(getFields());
    }

    @Override

    public void setErrorStyleNameForField(Field<?> field, String styleName) {
        errorStyleNameMap.put(field, styleName);
    }

    @Override
    public String getErrorStyleNameForField(Field<?> field) {
        return errorStyleNameMap.get(field);
    }
}
