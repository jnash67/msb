package com.medcognize.form.field;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.medcognize.domain.basic.DisplayFriendly;
import com.vaadin.data.util.converter.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Locale;

// We extend Converter<Object, E> and not Converter<String, E> because we use this for
// OptionGroup.setConverter() which won't accept Converter<String, E>.  It expects
// Converter<Object, ?>.
public class StringToDisplayFriendlyEnumConverter<E extends Enum<E>> implements Converter<Object, E> {
    protected final static Logger LOGGER = LoggerFactory.getLogger(StringToDisplayFriendlyEnumConverter.class);
    final Class<E> enumClazz;
    ArrayList<String> options;
    BiMap<String, E> stringMap = HashBiMap.create();

    public StringToDisplayFriendlyEnumConverter(Class<E> enumClazz) {
        if (!DisplayFriendly.isDisplayFriendlyEnum(enumClazz)) {
            LOGGER.error("Using StringToDisplayFriendlyEnumConverter for non-DisplayFriendly Enum " + enumClazz
                    .getSimpleName());
        }
        this.enumClazz = enumClazz;
        generateOptionsFromEnum();
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    private void generateOptionsFromEnum() {
        this.options = new ArrayList<>();
        String caption;
        for (E enumVal : enumClazz.getEnumConstants()) {
            caption = DisplayFriendly.getEnumCaption(enumVal);
            if (null == caption) {
                options.add(enumVal.toString());
                stringMap.put(enumVal.toString(), enumVal);
            } else {
                options.add(caption);
                stringMap.put(caption, enumVal);
            }
        }
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
        return enumClazz;
    }

    @Override
    public Class<Object> getPresentationType() {
        return Object.class;
    }
}
