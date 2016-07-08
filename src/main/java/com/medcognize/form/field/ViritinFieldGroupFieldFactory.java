package com.medcognize.form.field;

import com.medcognize.domain.basic.DisplayFriendly;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.ui.Field;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.switchui.Switch;
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
        if (Integer.class.isAssignableFrom(dataType) || int.class.isAssignableFrom(dataType)) {
            IntegerField field = new IntegerField();
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
            return (T) field;
        }
        if (Boolean.class.isAssignableFrom(dataType)
                || boolean.class.isAssignableFrom(dataType)) {
//            MCheckBox field = new MCheckBox("");
//            return (T) field;
            return (T) new Switch();
        }
        if (String.class.isAssignableFrom(dataType)) {
            MTextField field = new MTextField();
            field.setNullRepresentation("");
            return (T) field;
        }
        if (Double.class.isAssignableFrom(dataType) || double.class.isAssignableFrom(dataType)) {
            MTextField field = new MTextField();
            field.setNullRepresentation("0.00");
            field.addValidator(new DoubleRangeValidator("Not a valid numeric value", null, null));
            field.setConverter(new StringToDoubleConverter());
            return (T) field;
        }
        T field = super.createField(dataType, fieldType);
        return field;
    }
}

