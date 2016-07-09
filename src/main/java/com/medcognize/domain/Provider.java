package com.medcognize.domain;

import com.google.common.collect.BiMap;
import com.medcognize.domain.basic.ContactInfo;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.basic.DisplayName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
// a provider equals another if it has the same name.  The names should be unique.  If you have
// two Dr. Smiths, name the second "Dr. Smith 2" or something
@EqualsAndHashCode(callSuper = false, of = {"providerName"})
@Entity
public class Provider extends DisplayFriendlyAbstractEntity implements Serializable {

	public static enum ProviderType {
		PRIMARY, SPECIALIST, PHARMACY, HOSPITAL, OTHER;
	}

	public static final ProviderType defaultProviderType = ProviderType.PRIMARY;
	private static final String providerTypeCaptionString = ProviderType.PRIMARY.toString() + ":Primary Care Doctor," + " " + "" + ProviderType.SPECIALIST.toString() + ":Specialist," + ProviderType.PHARMACY.toString() + ":Pharmacy, " + "" + ProviderType.HOSPITAL.toString() + ":Hospital," + ProviderType.OTHER.toString() + ":Other";
	public static final BiMap<String, String> providerTypeStringMap = createBiMap(providerTypeCaptionString);

	@NotBlank(message = "The provider name cannot be blank")
	private String providerName = "";
	@DisplayName("In Plan")
	private boolean providerInPlan;
	private ProviderType providerType = defaultProviderType;
	private String providerId = "";

	@ElementCollection(fetch = FetchType.EAGER)
	private List<ContactInfo> locations = new ArrayList<ContactInfo>();

	@Override
	public String toString() {
		return providerName;
	}
}
