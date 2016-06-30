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
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.fields.*;

import java.util.Date;
import java.util.Locale;

@Slf4j
public class ViritinFieldGroupFieldFactory extends DefaultFieldGroupFieldFactory {
    public ViritinFieldGroupFieldFactory() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Field> T createField(Class<?> dataType, Class<T> fieldType) {
        if (Integer.class.isAssignableFrom(dataType)) {
            IntegerSliderField field = new IntegerSliderField();
            field.setImmediate(true);
            return (T) field;
        }
        if (Date.class.isAssignableFrom(dataType)) {
            MDateField field = new MDateField();
            field.setLocale(Locale.US);
            return (T) field;
        }
        if (Enum.class.isAssignableFrom(dataType)) {
            EnumSelect field = new EnumSelect();
            return (T) field;
        }
        if (DisplayFriendly.class.isAssignableFrom(dataType)) {
            TypedSelect field = new TypedSelect();
            if (Provider.class.isAssignableFrom(dataType)) {
                field.setOptions(UserUtil.getAll(DbUtil.getLoggedInUser(), Provider.class));
            } else if (FamilyMember.class.isAssignableFrom(dataType)) {
                field.setOptions(UserUtil.getAll(DbUtil.getLoggedInUser(), FamilyMember.class));
            } else if (Plan.class.isAssignableFrom(dataType)) {
                field.setOptions(UserUtil.getAll(DbUtil.getLoggedInUser(), Plan.class));
            }
            return (T) field;
        }
//        else if (Boolean.class.isAssignableFrom(dataType)
//                || boolean.class.isAssignableFrom(dataType)) {
//            return (T) new MCheckBox();
//        }
        if (String.class.isAssignableFrom(fieldType)) {
            MTextField field = new MTextField();
            field.setNullRepresentation("");
            return (T) field;
        }
        T field = super.createField(dataType, fieldType);
        if (Double.class.isAssignableFrom(dataType)) {
            field.addValidator(new DoubleRangeValidator("Not a valid value", null, null));
        }
        return field;
    }
}
