package com.medcognize;

import com.medcognize.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

	@Query("SELECT COUNT(username) FROM User WHERE 'ROLE_ADMIN' MEMBER OF userRoles")
	long countByAdminTrue();

	// capitalization of U in 'User' matters
	@Query("SELECT CASE WHEN COUNT(u.username) > 0 THEN true ELSE false END FROM User u WHERE 'ROLE_ADMIN' MEMBER OF userRoles")
	boolean existsByAdminTrue();

	// capitalization of U in 'User' matters
	@Query("SELECT CASE WHEN COUNT(u.username) > 0 THEN true ELSE false END FROM User u WHERE u.username = ?1")
	boolean existsByUsername(String username);

	List<User> findByLastNameStartsWithIgnoreCase(String lastName);

	// capitalization of U in 'User' matters
	@Query("select u.password from User u where u.username = ?1")
	String findPasswordForUsername(String username);

	User findById(Long id);


}
