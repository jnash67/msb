// Generated by delombok at Sun May 08 23:43:45 EDT 2016
package com.medcognize.domain;

import com.google.common.collect.BiMap;
import com.medcognize.UserService;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.basic.EmailAddress;
import com.medcognize.util.PasswordHash;
import com.vaadin.spring.annotation.VaadinSessionScope;
import org.hibernate.validator.constraints.Email;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
// A user is equal to another if they have the same email address
@Entity
@VaadinSessionScope
public class User extends DisplayFriendlyAbstractEntity {
	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(User.class);
	private static final String captionString = "username:Username, password:Password, firstName:First Name, lastName:Last Name, admin:Admin";
	@SuppressWarnings("UnusedDeclaration")
	public static final BiMap<String, String> captionMap = createBiMap(captionString);
	// This bean must have a no-args constructor so we can't declare this final.  We have to make sure
	// the UserService is set when Hibernate creates a User bean from the DB (typically when logging in).
	@Transient
	private UserService repo;
	@Column(unique = true)
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
	private String password = "";
	/*
	Having FetchType.LAZY with Spring Boot and Hibernate is a problem.  It tends to run into LazyInitializationExceptions
	because you often access the collections after the User has been loaded and the DB transaction loading
	the user has already been closed.  Trying to get all the data access to happen under an
	@Transactional annotation in the repo is tricky because you tend to access them at different times.

	Adding a servlet filter is tricky in Spring Boot because most examples are using web.xml and we are doing
	jar deployment in the embedded Spring Boot tomcat server.  Also, it forces you to get way too involved
	with underlying Spring stuff which defeats the point of using Spring Boot.

	The final option is to use FetchType.EAGER which loads all the data upfront which can be a problem if there's
	a lot of data.  Shouldn't be an issue for a while for this User class.

	Tried for a few days to get FetchType.LAZY working with little success. So ended up going with EAGER.  One
	eventual solution would be to move Plans into a table not controlled by User and have Plans control MedicalExpenses.
	Currently User controls everything which makes things simpler.

	Some resources I consulted:
	See: http://stackoverflow.com/questions/11746499/solve-failed-to-lazily-initialize-a-collection-of-role-exception
	http://stackoverflow.com/questions/4306463/how-to-test-whether-lazy-loaded-jpa-collection-is-initialized
	http://stackoverflow.com/questions/19825946/how-to-add-a-filter-class-in-spring-boot
	http://stackoverflow.com/questions/14390823/getting-no-bean-named-sessionfactory-error-when-using-opensessioninviewfilter
	*/
	/*
	Note: CascadeType determines what will happen if THIS entity is REMOVED, PERSISTED, REFRESHED, DETACHED or
	MERGED.  So if we REMOVE this User,	all it's children will also be removed from their respective tables.  However,
	if we just remove one of the children from the collection, no cascading happens. orphanRemoval ensures the children
	removed from the collection are also removed from the DB.  If we PERSIST this entity, all it's children will be
	persisted as well, so adding children to the table does result in their being persisted.
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<FamilyMember> familyMembers = new ArrayList<FamilyMember>();
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Plan> plans = new ArrayList<Plan>();
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<MedicalExpense> medicalExpenses = new ArrayList<MedicalExpense>();
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Provider> providers = new ArrayList<Provider>();
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Fsa> fsas = new ArrayList<Fsa>();

	// enables registration
	public User(final UserService repo, final EmailAddress email, final String pwd) {
		this.repo = repo;
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

	public String getPassword() {
		return this.password;
	}

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

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public UserService getRepo() {
		return this.repo;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public boolean isAdmin() {
		return this.admin;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public String getFirstName() {
		return this.firstName;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public String getLastName() {
		return this.lastName;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public List<FamilyMember> getFamilyMembers() {
		return this.familyMembers;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public List<Plan> getPlans() {
		return this.plans;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public List<MedicalExpense> getMedicalExpenses() {
		return this.medicalExpenses;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public List<Provider> getProviders() {
		return this.providers;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public List<Fsa> getFsas() {
		return this.fsas;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setRepo(final UserService repo) {
		this.repo = repo;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setUsername(final String username) {
		this.username = username;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setAccountNonExpired(final boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setAccountNonLocked(final boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setCredentialsNonExpired(final boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setAdmin(final boolean admin) {
		this.admin = admin;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setFamilyMembers(final List<FamilyMember> familyMembers) {
		this.familyMembers = familyMembers;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setPlans(final List<Plan> plans) {
		this.plans = plans;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setMedicalExpenses(final List<MedicalExpense> medicalExpenses) {
		this.medicalExpenses = medicalExpenses;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setProviders(final List<Provider> providers) {
		this.providers = providers;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public void setFsas(final List<Fsa> fsas) {
		this.fsas = fsas;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) return false;
		final User other = (User) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		final java.lang.Object this$username = this.getUsername();
		final java.lang.Object other$username = other.getUsername();
		if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof User;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $username = this.getUsername();
		result = result * PRIME + ($username == null ? 43 : $username.hashCode());
		return result;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public User() {
	}
}
