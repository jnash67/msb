package com.medcognize.form.field;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.ui.TextField;

import java.text.NumberFormat;
import java.util.Locale;

@SuppressWarnings("UnusedDeclaration")
public class IntegerField extends TextField {

	// we don't want a comma to show up in the value for the year
	protected StringToIntegerConverter plainIntegerConverter = new StringToIntegerConverter() {
		protected NumberFormat getFormat(Locale locale) {
			NumberFormat format = super.getFormat(locale);
			format.setGroupingUsed(false);
			return format;
		}
	};

	public IntegerField() {
		this.setConverter(plainIntegerConverter);
	}

	public IntegerField(String caption) {
		super(caption);
		this.setConverter(plainIntegerConverter);
	}

	public IntegerField(Property dataSource) {
		super(dataSource);
		this.setConverter(plainIntegerConverter);
	}

	public IntegerField(String caption, Property dataSource) {
		super(caption, dataSource);
		this.setConverter(plainIntegerConverter);
	}

	public IntegerField(String caption, String value) {
		super(caption, value);
		this.setConverter(plainIntegerConverter);
	}

}
