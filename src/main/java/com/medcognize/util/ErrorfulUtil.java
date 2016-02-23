package com.medcognize.util;

import com.medcognize.form.field.errorful.ErrorfulFieldLayout;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.vaadin.addon.daterangefield.DateRangeField;
import org.vaadin.risto.stepper.Stepper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class ErrorfulUtil implements Serializable {

    public static String errorStyleName = "errorstyle";
    public static String stepperErrorStyleName = "steppererrorstyle";

    public static void addErrorChangeListener(final Component c, final ErrorfulFieldLayout efl) {
        if (c instanceof Field) {
            final Field<?> f = (Field<?>) c;
            List<Field<?>> fieldList = efl.getFields();
            if (!fieldList.contains(f)) {
                fieldList.add(f);
                if (f instanceof DateRangeField) {
                    efl.setErrorStyleNameForField(f, stepperErrorStyleName);
                } else if (f instanceof Stepper) {
                    efl.setErrorStyleNameForField(f, stepperErrorStyleName);
                } else {
                    efl.setErrorStyleNameForField(f, errorStyleName);
                }
                f.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        if (efl.getExcludedFields().contains(f)) {
                            return;
                        }
                        try {
                            f.validate();
                            f.removeStyleName(efl.getErrorStyleNameForField(f));
                        } catch (Validator.InvalidValueException ive) {
                            f.addStyleName(efl.getErrorStyleNameForField(f));
                        }
                    }
                });
            }
        }
    }

    // this enables an initial validation before the fields have changed
    public static Validator.InvalidValueException highlightInvalidFields(Collection<Field<?>> fieldsCollection,
                                                                         final ErrorfulFieldLayout efl) {
        Validator.InvalidValueException first = null;
        for (Field<?> f : fieldsCollection) {
            if (!efl.getExcludedFields().contains(f)) {
                try {
                    f.validate();
                    f.removeStyleName(efl.getErrorStyleNameForField(f));
                } catch (Validator.InvalidValueException ive) {
                    if (null == first) {
                        first = ive;
                    }
                    f.addStyleName(efl.getErrorStyleNameForField(f));
                }
            }
        }
        return first;
    }

    public static void discardInvalidBufferedValues(Collection<Field<?>> fieldsCollection) {
        for (Field<?> f : fieldsCollection) {
            try {
                f.validate();
            } catch (Validator.InvalidValueException ive) {
                f.discard();
            }
        }
    }

}
