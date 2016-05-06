package com.medcognize.domain;

import com.google.common.collect.BiMap;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.basic.DisplayFriendlyCollectionManager;
import com.medcognize.domain.basic.DisplayFriendlyCollectionOwner;
import com.medcognize.domain.basic.EmailAddress;
import com.medcognize.util.PasswordHash;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Email;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Data
// A user is equal to another if they have the same email address
@EqualsAndHashCode(callSuper = false, of = {"username"})
@NoArgsConstructor
@Entity
@Slf4j
public class User extends DisplayFriendlyAbstractEntity implements DisplayFriendlyCollectionOwner {

	private static final String captionString = "username:Username, password:Password, firstName:First Name, " +
	 		"lastName:Last Name, admin:Admin";
	@SuppressWarnings("UnusedDeclaration")
	public static final BiMap<String, String> captionMap = createBiMap(captionString);

	@Column(unique=true)
	@Email
	private String username = "";

	private boolean accountNonExpired = false;
	private boolean accountNonLocked = false;
	private boolean credentialsNonExpired = false;
	private boolean enabled = false;
	private boolean admin = false;

	private String firstName = "";

	private String lastName = "";

	// the password is never stored in this field, only the hash
	@Setter(AccessLevel.NONE)
	private String password =  "";

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<FamilyMember> familyMembers;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Plan> plans;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<MedicalExpense> medicalExpenses;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Provider> providers;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Fsa> fsas;

	// enables registration
	public User(final EmailAddress email, final String pwd) {
		this.username = email.toString();
		try {
			this.password = PasswordHash.createHash(pwd);
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException in constructor: " + e);
		} catch (InvalidKeySpecException e) {
			log.error("InvalidKeySpecException in constructor: " + e);
		}
	}

	public static boolean validateUserPassword(String existingHash, String passwordToTry) {
		boolean v = false;
		try {
			v = PasswordHash.validatePassword(passwordToTry, existingHash);
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException in validate: " + e);
		} catch (InvalidKeySpecException e) {
			log.error("InvalidKeySpecException in validate: " + e);
		}
		return v;
	}

	@Override
	public String toString() {
		return getUsername();
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {return this.password; }

	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void add(DisplayFriendly ownedItem) {
		DisplayFriendlyCollectionManager.add(this, ownedItem);
	}

	@Override
	public void remove(DisplayFriendly ownedItem) {
		DisplayFriendlyCollectionManager.remove(this, ownedItem);
	}

	@Override
	public <T extends DisplayFriendly> List<T> getAll(Class<T> clazz) {
		return DisplayFriendlyCollectionManager.getAll(this, clazz);
	}

	public void merge(User copyFrom) {
		DisplayFriendlyCollectionManager.merge(this, copyFrom);
	}

	public Plan getActivePlan() {
		return DisplayFriendlyCollectionManager.getActivePlan(this);
	}

	public void setActivePlan(Plan ap) {
		DisplayFriendlyCollectionManager.setActivePlan(this, ap);
	}

	public void deleteMedicalExpensesForPlan(Plan ap) {
		DisplayFriendlyCollectionManager.deleteMedicalExpensesForPlan(this, ap);
	}

	public List<MedicalExpense> getMedicalExpensesForPlan(Plan ap) {
		return DisplayFriendlyCollectionManager.getMedicalExpensesForPlan(this, ap);
	}

	public List<FamilyMember> getFamilyMembersWithPlanExpenses(Plan ap) {
		return DisplayFriendlyCollectionManager.getFamilyMembersWithPlanExpenses(this, ap);
	}

	// Create relevant defaults for the required user entities
	public void createDefaultInitialSettings() {
		FamilyMember fm;
		Plan p;

		fm = new FamilyMember();
		String email = this.getUsername();
		String firstName = this.getFirstName();
		if (null == firstName) {
			firstName = "";
		}
		if ("".equals(firstName)) {
			// now create the first family member and the plan for the user
			firstName = email.substring(0, email.indexOf("@"));
		}
		fm.setFamilyMemberName(firstName);
		// this saves fm and then u
		this.add(fm);

		p = new Plan();
		String planName = firstName + "'s Plan";
		if (planName.length() > 50) {
			p.setPlanName(planName.substring(0, 50));
		} else {
			p.setPlanName(planName);
		}
		// this saves p and then u
		p.setActivePlan(true);
		this.add(p);
	}
}