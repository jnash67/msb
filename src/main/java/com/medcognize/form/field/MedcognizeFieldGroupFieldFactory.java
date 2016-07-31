package com.medcognize.form.field;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.Plan;
import com.medcognize.domain.Provider;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.ui.Field;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.risto.stepper.DateStepper;
import org.vaadin.risto.stepper.IntStepper;

import java.util.Date;
import java.util.Locale;

@Slf4j
public class MedcognizeFieldGroupFieldFactory extends DefaultFieldGroupFieldFactory {

    public MedcognizeFieldGroupFieldFactory() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Field> T createField(Class<?> dataType, Class<T> fieldType) {
        if (Integer.class.isAssignableFrom(dataType)) {
            IntStepper field = new IntStepper();
            field.setMouseWheelEnabled(true);
            field.setImmediate(true);
            return (T) field;
        }
        if (Date.class.isAssignableFrom(dataType)) {
            DateStepper field = new DateStepper();
            field.setMouseWheelEnabled(true);
            field.setImmediate(true);
            // locale must be set or it craps out
            field.setLocale(Locale.US);
            return (T) field;
        }
        if (Enum.class.isAssignableFrom(dataType)) {
            EnumOptionGroup field = new EnumOptionGroup(dataType);
            field.setImmediate(true);
            return (T) field;
        }
        if (DisplayFriendly.class.isAssignableFrom(dataType)) {
            NativeSelect field;
            if (Provider.class.isAssignableFrom(dataType)) {
                field = new NativeSelect("", UserUtil.getAllFromUser(DbUtil.getLoggedInUser(), Provider.class));
            } else if (FamilyMember.class.isAssignableFrom(dataType)) {
                field = new NativeSelect("", UserUtil.getAllFromUser(DbUtil.getLoggedInUser(), FamilyMember.class));
            } else if (Plan.class.isAssignableFrom(dataType)) {
                field = new NativeSelect("", UserUtil.getAllFromUser(DbUtil.getLoggedInUser(), Plan.class));
            } else {
                field = new NativeSelect();
            }
            field.setImmediate(true);
            return (T) field;
        }
        T field = super.createField(dataType, fieldType);
        if (field instanceof TextField) {
            ((TextField) field).setNullRepresentation("");
        }
        if (Double.class.isAssignableFrom(dataType)) {
            field.addValidator(new DoubleRangeValidator("Not a valid value", null, null));
        }
        return field;
    }
}