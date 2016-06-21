package com.medcognize.domain;

import com.google.common.collect.BiMap;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.validator.jsr303.PhoneNumberOrBlank;
import com.medcognize.domain.validator.jsr303.UnitedStatesStateOrBlank;
import com.medcognize.domain.validator.jsr303.ZipCodeOrBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@NoArgsConstructor
// a provider equals another if it has the same name.  The names should be unique.  If you have
// two Dr. Smiths, name the second "Dr. Smith 2" or something
@EqualsAndHashCode(callSuper = false, of = {"providerName"})
@Entity
public class Provider extends DisplayFriendlyAbstractEntity implements Serializable {
	private static final String captionString = "providerName:Name, providerInPlan:In Plan, providerType:Provider Type, providerId:Provider Id, address1:Address 1, address2:Address 2, city:City, state:State, zip:Zip, phoneNumber:Phone Number, website:Website";

	public static enum ProviderType {
		PRIMARY, SPECIALIST, PHARMACY, HOSPITAL, OTHER;
	}

	public static final ProviderType defaultProviderType = ProviderType.PRIMARY;
	private static final String providerTypeCaptionString = ProviderType.PRIMARY.toString() + ":Primary Care Doctor," + " " + "" + ProviderType.SPECIALIST.toString() + ":Specialist," + ProviderType.PHARMACY.toString() + ":Pharmacy, " + "" + ProviderType.HOSPITAL.toString() + ":Hospital," + ProviderType.OTHER.toString() + ":Other";
	@SuppressWarnings("UnusedDeclaration")
	public static final BiMap<String, String> captionMap = createBiMap(captionString);
	public static final BiMap<String, String> providerTypeStringMap = createBiMap(providerTypeCaptionString);
	// was previously "name" and FamilyMember also had a "name" field which caused a conflict with JPA. So
	// changed it to be more specific. Not an issue with Objectify.
	@NotEmpty
	private String providerName = "";
	private boolean providerInPlan;
	private ProviderType providerType = defaultProviderType;
	private String providerId = "";
	private String address1 = "";
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
		return providerName;
	}
}
