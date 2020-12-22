package org.covid.inventory.datajpa;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.covid.inventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * This method gets active user by usrname
	 * 
	 * @param username
	 * @return User
	 */
	@Query("SELECT u FROM User u WHERE u.username = ?1 and u.isActive = 1")
	User findActiveUserByUsername(String username);
	
	/**
	 * This method gets active user by usrname
	 * 
	 * @param username
	 * @return User
	 */
	@Query("SELECT u FROM User u WHERE u.username = ?1")
	User findUserByUsername(String username);
	
	/**
	 * This method gets active user by usrname
	 * 
	 * @param username
	 * @return User
	 */
	@Query("SELECT u FROM User u WHERE u.emailId = ?1")
	User findUserByEmailId(String username);
	
	/**
	 * This method gets active user by emailid
	 * 
	 * @param username
	 * @return User
	 */
	@Query("SELECT u FROM User u WHERE u.emailId = ?1 and u.isActive = 1")
	User findActiveUserByEmailId(String emailId);
	
	/**
	 * This method gets active user by mobileNumber
	 * 
	 * @param username
	 * @return User
	 */
	@Query("SELECT u FROM User u WHERE u.mobileNumber = ?1 and u.isActive = 1")
	User findActiveUserByMobileNumber(String mobileNumber);

	/**
	 * This method gets active users from db.
	 * 
	 * @return List of users
	 */
	@Query(value = "FROM User u WHERE u.isActive=1")
	ArrayList<User> getAllActiveUsers();

	/**
	 * This method checks if the user exists by usrname given
	 * 
	 * @param username
	 * @return true if exists else false
	 */
	boolean existsByUsername(String username);
	
	/**
	 * This method checks if the user exists by emailId given
	 * 
	 * @param emailId
	 * @return true if exists else false
	 */
	boolean existsByEmailId(String emailId);

	/**
	 * This method soft deletes user by given id
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE User u SET u.isActive=0 WHERE u.id=?1")
	void softDeleteUserById(Integer id);
	
	@Query("SELECT u FROM User u WHERE u.id = ?1")
	User findUserById(Integer id);
	
	/**
	 * This method gets all users from db.
	 * 
	 * @return List of users
	 */
	@Query(value = "FROM User u")
	ArrayList<User> getAllUsers();
	
	@Query(value = "SELECT * FROM user WHERE first_name = ?1", nativeQuery = true)
	List<Integer> findUserIdByFirstname(String fname);
	
	@Query(value = "SELECT * FROM user ORDER BY first_name ASC", nativeQuery = true)
	List<User> getAllCustomers();
	
	@Query(value = "SELECT * FROM user WHERE is_active = ?1 ORDER BY first_name ASC", nativeQuery = true)
	List<User> getAllActiveOrInactiveCustomers(Boolean isActive);
	
	@Query(value="SELECT * FROM user WHERE store_id = ?1 AND role_id=3", nativeQuery = true)
	 List<User> findAggregatorsForStore(Integer id);
}