package com.medcognize.domain;

import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.basic.DisplayFriendlyCaption;
import com.vaadin.spring.annotation.VaadinSessionScope;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
// A user is equal to another if they have the same email address
@EqualsAndHashCode(callSuper = false, of = {"username"})
@NoArgsConstructor
@Entity
@VaadinSessionScope
@Slf4j
public class User extends DisplayFriendlyAbstractEntity implements UserDetails {

	@Column(unique = true)
	@Email
	@DisplayFriendlyCaption("Username")
	private String username = "";

	// For UserDetails
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> userRoles = new ArrayList<String>();
	private boolean accountNonExpired = false;
	private boolean accountNonLocked = false;
	private boolean credentialsNonExpired = false;
	private boolean enabled = false;

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

	@Override
	public String toString() {
		return getUsername();
	}

	public String getPassword() {
		return this.password;
	}

	// FOR IMPLEMENTATION OF UserDetails

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String roles = StringUtils.collectionToCommaDelimitedString(userRoles);
		return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
	}
}
