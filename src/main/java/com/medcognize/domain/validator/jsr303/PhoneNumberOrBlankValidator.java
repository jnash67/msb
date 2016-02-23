package com.medcognize.domain.validator.jsr303;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberOrBlankValidator implements ConstraintValidator<PhoneNumberOrBlank, String> {

	@Override
	public void initialize(PhoneNumberOrBlank phoneNumber) {
	}

	@Override
	public boolean isValid(String string, ConstraintValidatorContext context) {
		if (null == string) {
			return true;
		}
		if (0 == string.length()) {
			return true;
		}
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
			phoneUtil.parse(string, "US");
		} catch (NumberParseException e) {
			return false;
		}
		return true;
	}

}