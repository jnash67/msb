package com.medcognize.domain;

import com.medcognize.domain.basic.ContactInfo;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.basic.DisplayFriendlyCaption;
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
		PRIMARY("Primary"), SPECIALIST("Specialist"), PHARMACY("Pharmacy"), HOSPITAL("Hospital"), OTHER("Other");
		private String name;

		ProviderType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static final ProviderType defaultProviderType = ProviderType.PRIMARY;
	@NotBlank(message = "The provider name cannot be blank")
	private String providerName = "";
	@DisplayFriendlyCaption("In Plan")
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
