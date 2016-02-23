package com.medcognize.form.field.errorful;

import com.medcognize.util.ErrorfulUtil;
import com.vaadin.data.Validator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorfulGridLayout extends GridLayout implements ErrorfulFieldLayout {

    private List<Field<?>> fieldList = new ArrayList<>();
    private List<Field<?>> excludedFieldList = new ArrayList<>();
    private Map<Field<?>, String> errorStyleNameMap = new HashMap<>();

    public ErrorfulGridLayout(int columns, int rows) {
        super(columns, rows);
    }

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

    public void addComponent(Component c, int column, int row) throws OverlapsException, OutOfBoundsException {
        ErrorfulUtil.addErrorChangeListener(c, this);
        super.addComponent(c, column, row);
    }

    @Override
    public void addComponent(Component c, int column1, int row1, int column2, int row2) throws OverlapsException,
            OutOfBoundsException {
        ErrorfulUtil.addErrorChangeListener(c, this);
        super.addComponent(c, column1, row1, column2, row2);
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
