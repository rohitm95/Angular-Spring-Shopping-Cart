package org.covid.inventory.datajpa;

import org.covid.inventory.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	/**
	 * 
	 * @param username
	 * @return
	 */
	@Query("SELECT r FROM Role r WHERE UPPER(r.name) = UPPER(?1)")
	Role getRoleByName(String name);
	
	/**
	 * 
	 * @param roleId
	 * @return
	 */
	@Query("SELECT r FROM Role r WHERE UPPER(r.id) = ?1")
	Role getRoleById(int roleId);
	
}
