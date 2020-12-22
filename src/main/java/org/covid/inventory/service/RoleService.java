package org.covid.inventory.service;

import java.util.List;

import org.covid.inventory.entity.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {

	List<Role> getAllRoles();
	
	Role getRoleByName(String role);
	
	Role getRoleById(int roleId);
}
