package com.medcognize;

import com.medcognize.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
Here we define all the easy queries that come for free with Spring Boot.  UserService however
needs to make those available since it is the only one with an instance of UserRepository
and UserService is Autowired throughout (or otherwise passed).  So there should only be two
references to UserRepository in the code, one here and one in UserService.
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	long countByUsername(String username);

//	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
//	boolean existsByUsername(@Param("username") String username);

	User findByUsername(String username);

	// was: @Query("SELECT COUNT(username) FROM User WHERE 'ROLE_ADMIN' MEMBER OF userRoles")
	// @Query("SELECT COUNT(u.username) FROM User u WHERE 'ROLE_ADMIN' MEMBER OF u.userRoles")
	@Query("SELECT COUNT(u.username) FROM User u inner join u.userRoles ur where ur = 'ROLE_ADMIN'")
	long countByAdminTrue();

	// capitalization of U in 'User' matters
	// was: @Query("SELECT CASE WHEN COUNT(u.username) > 0 THEN true ELSE false END FROM User u WHERE 'ROLE_ADMIN' MEMBER OF u.userRoles")
	@Query("SELECT CASE WHEN COUNT(u.username) > 0 THEN true ELSE false END FROM User u " +
			"inner join u.userRoles ur where ur = 'ROLE_ADMIN'")
	boolean existsByAdminTrue();

	// capitalization of U in 'User' matters
	@Query("SELECT CASE WHEN COUNT(u.username) > 0 THEN true ELSE false END FROM User u WHERE u.username = ?1")
	boolean existsByUsername(String username);

	List<User> findByLastNameStartsWithIgnoreCase(String lastName);

	// capitalization of U in 'User' matters
	@Query("select u.password from User u where u.username = ?1")
	String findPasswordForUsername(String username);

	User findById(Long id);

	@Query("select u.medicalExpenses from User u where u.username = :username")
	List<MedicalExpense> findMedicalExpenses(@Param("username") String username);

	@Query("select u.fsas from User u where u.username = :username")
	List<Fsa> findFsas(@Param("username") String username);

	@Query("select u.providers from User u where u.username = :username")
	List<Provider> findProviders(@Param("username") String username);

	@Query("select u.plans from User u where u.username = :username")
	List<Plan> findPlans(@Param("username") String username);

	@Query("select u.familyMembers from User u where u.username = :username")
	List<FamilyMember> findFamilyMembers(@Param("username") String username);
}
