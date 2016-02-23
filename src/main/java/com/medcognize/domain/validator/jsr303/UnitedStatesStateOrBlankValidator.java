package com.medcognize.domain.validator.jsr303;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

// from https://stackoverflow.com/questions/11005751/is-there-a-util-to-convert-us-state-name-to-state-code-eg
// -arizona-to-az
@SuppressWarnings("UnusedDeclaration")
public class UnitedStatesStateOrBlankValidator implements ConstraintValidator<UnitedStatesStateOrBlank, String> {

    private static final BiMap<String, String> states = HashBiMap.create();

    static {
        states.put("Alabama", "AL");
        states.put("Alaska", "AK");
        states.put("American Samoa", "AS");
        states.put("Arizona", "AZ");
        states.put("Arkansas", "AR");
        states.put("Armed Forces (AE)", "AE");
        states.put("Armed Forces Americas", "AA");
        states.put("Armed Forces Pacific", "AP");
        states.put("California", "CA");
        states.put("Colorado", "CO");
        states.put("Connecticut", "CT");
        states.put("Delaware", "DE");
        states.put("District Of Columbia", "DC");
        states.put("Florida", "FL");
        states.put("Georgia", "GA");
        states.put("Guam", "GU");
        states.put("Hawaii", "HI");
        states.put("Idaho", "ID");
        states.put("Illinois", "IL");
        states.put("Indiana", "IN");
        states.put("Iowa", "IA");
        states.put("Kansas", "KS");
        states.put("Kentucky", "KY");
        states.put("Louisiana", "LA");
        states.put("Maine", "ME");
        states.put("Maryland", "MD");
        states.put("Massachusetts", "MA");
        states.put("Michigan", "MI");
        states.put("Minnesota", "MN");
        states.put("Mississippi", "MS");
        states.put("Missouri", "MO");
        states.put("Montana", "MT");
        states.put("Nebraska", "NE");
        states.put("Nevada", "NV");
        states.put("New Hampshire", "NH");
        states.put("New Jersey", "NJ");
        states.put("New Mexico", "NM");
        states.put("New York", "NY");
        states.put("North Carolina", "NC");
        states.put("North Dakota", "ND");
        states.put("Ohio", "OH");
        states.put("Oklahoma", "OK");
        states.put("Oregon", "OR");
        states.put("Pennsylvania", "PA");
        states.put("Puerto Rico", "PR");
        states.put("Rhode Island", "RI");
        states.put("South Carolina", "SC");
        states.put("South Dakota", "SD");
        states.put("Tennessee", "TN");
        states.put("Texas", "TX");
        states.put("Utah", "UT");
        states.put("Vermont", "VT");
        states.put("Virgin Islands", "VI");
        states.put("Virginia", "VA");
        states.put("Washington", "WA");
        states.put("West Virginia", "WV");
        states.put("Wisconsin", "WI");
        states.put("Wyoming", "WY");
    }

    public static Collection<String> getAllStateNames() {
        return states.keySet();
    }

    public static Collection<String> getAllStateCodes() {
        return states.values();
    }

    @Override
    public void initialize(UnitedStatesStateOrBlank state) {
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext context) {
        if (null == string) {
            return true;
        }
	    //noinspection SimplifiableIfStatement
	    if (0 == string.length()) {
            return true;
        }
	    return getAllStateNames().contains(string);
    }

}