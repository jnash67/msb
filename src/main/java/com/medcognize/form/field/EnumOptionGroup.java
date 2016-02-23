package com.medcognize.form.field;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.OptionGroup;

import java.util.Locale;

public class EnumOptionGroup<E extends Enum<E>> extends OptionGroup implements Converter<Object, E> {
    final Class<E> enumClass;
    BiMap<String, E> stringMap = HashBiMap.create();

    public EnumOptionGroup(Class<E> enumClass) {
        super();
        this.enumClass = enumClass;
        for (E enumVal : enumClass.getEnumConstants()) {
            stringMap.put(enumVal.toString(), enumVal);
        }

        // code taken from AbstractSelect constructor
        // Creates the options container and add given options to it
        final Container c = new IndexedContainer();
        for (String obj : stringMap.keySet()) {
            c.addItem(obj);
        }
        this.setContainerDataSource(c);
    }


    @Override
    public E convertToModel(Object value, Class<? extends E> targetType, Locale locale) throws ConversionException {
        if (null != value) {
            return stringMap.get(value.toString());
        }
        return null;
    }

    @Override
    public Object convertToPresentation(E value, Class<?> targetType, Locale locale) throws ConversionException {
        return stringMap.inverse().get(value);
    }

    @Override
    public Class<E> getModelType() {
        return enumClass;
    }

    @Override
    public Class<Object> getPresentationType() {
        return Object.class;
    }

}
