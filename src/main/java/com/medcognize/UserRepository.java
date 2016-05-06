package com.medcognize;

import com.medcognize.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRepository extends JpaRepository<User, Long> {

	long countByUsername(String username);

//	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
//	boolean existsByUsername(@Param("username") String username);

	User findByUsername(String username);

	long countByAdminTrue();

	// capitalization of U in 'User' matters
	@Query("SELECT CASE WHEN COUNT(admin) > 0 THEN true ELSE false END FROM User WHERE admin = true")
	boolean existsByAdminTrue();

	// capitalization of U in 'User' matters
	@Query("SELECT CASE WHEN COUNT(u.username) > 0 THEN true ELSE false END FROM User u WHERE u.username = ?1")
	boolean existsByUsername(String username);

	User findByAdmin(boolean admin);
	List<User> findByLastNameStartsWithIgnoreCase(String lastName);

	// capitalization of U in 'User' matters
	@Query("select u.password from User u where u.username = ?1")
	String findPasswordForUsername(String username);
}
