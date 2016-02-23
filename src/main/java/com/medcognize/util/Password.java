package com.medcognize.util;

import com.vaadin.data.util.converter.Converter;

import java.io.Serializable;
import java.util.Locale;

/**
 * This is a pure tagging interface to allow the FieldGroupFieldFactory to
 * differentiate between a Password and a regular String field
 */
public class Password implements Serializable {

    public static class PasswordConverter implements Converter<String, Password> {

        @Override
        public Password convertToModel(String value, Class<? extends Password> targetType, Locale locale) throws
                ConversionException {
            return new Password(value);
        }

        @Override
        public String convertToPresentation(Password value, Class<? extends String> targetType,
                                            Locale locale) throws ConversionException {
            return value.getValue();
        }

        @Override
        public Class<Password> getModelType() {
            return Password.class;
        }

        @Override
        public Class<String> getPresentationType() {
            return String.class;
        }
    }

	String wrappedString;

	public Password() {
		this("");
	}

	public Password(String s) {
		this.wrappedString = s;
	}

	public String getValue() {
		return wrappedString;
	}

	public void setValue(String s) {
		wrappedString = s;
	}
}
