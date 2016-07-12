package com.medcognize.domain.basic;

import com.medcognize.domain.validator.jsr303.PhoneNumberOrBlank;
import com.medcognize.domain.validator.jsr303.UnitedStatesStateOrBlank;
import com.medcognize.domain.validator.jsr303.ZipCodeOrBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@NoArgsConstructor
// two addresses are equal if all the fields are equal
@EqualsAndHashCode(callSuper = false)
@Entity
@Embeddable
public class ContactInfo extends DisplayFriendlyAbstractEntity implements Serializable {

    @DisplayFriendlyCaption("Address 1")
    private String address1 = "";
    @DisplayFriendlyCaption("Address 2")
    private String address2 = "";
    private String city = "";
    @UnitedStatesStateOrBlank
    private String state = "";
    @ZipCodeOrBlank
    private String zip = "";
    @PhoneNumberOrBlank
    private String phoneNumber = "";
    @URL(message = "Not a valid URL")
    private String website;

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
