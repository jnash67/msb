package com.medcognize.domain.basic;

import com.google.common.collect.BiMap;
import com.medcognize.domain.validator.jsr303.PhoneNumberOrBlank;
import com.medcognize.domain.validator.jsr303.UnitedStatesStateOrBlank;
import com.medcognize.domain.validator.jsr303.ZipCodeOrBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
@NoArgsConstructor
// two addresses are equal if all the fields are equal
@EqualsAndHashCode(callSuper = false)
@Entity
public class Address extends DisplayFriendlyAbstractEntity implements Serializable {

    private static final String captionString = "address1:Address 1, address2:Address 2, city:City, state:State, zip:Zip, " +
            "" + "phoneNumber:Phone Number";
    @SuppressWarnings("UnusedDeclaration")
    public static final BiMap<String, String> captionMap = createBiMap(captionString);

    private String address1 = "";
    private String address2 = "";
    private String city = "";
    @UnitedStatesStateOrBlank
    private String state = "";
    @ZipCodeOrBlank
    private String zip = "";
    @PhoneNumberOrBlank
    private String phoneNumber = "";

    @Override
    public String toString() {
        if ((null == address1) || ("".equals(address1))) {
            if ((null == city) || ("".equals(city))) {
                return state + " " + zip;
            }
        }
        return address1 + ", " + city + ", " + state + " " + zip;
    }
}
