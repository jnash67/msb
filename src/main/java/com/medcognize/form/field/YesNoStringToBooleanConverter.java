package com.medcognize.form.field;

import com.vaadin.data.util.converter.StringToBooleanConverter;

public class YesNoStringToBooleanConverter extends StringToBooleanConverter {

    @Override
    protected String getTrueString() {
        return "Yes";
    }

    @Override
    protected String getFalseString() {
        return "No";
    }
}
