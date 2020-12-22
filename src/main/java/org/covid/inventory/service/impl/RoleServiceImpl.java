package org.covid.inventory.service.impl;

import java.util.List;

import org.covid.inventory.datajpa.RoleRepository;
import org.covid.inventory.entity.Role;
import org.covid.inventory.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	@Override
	public Role getRoleByName(String name) {
		return roleRepository.getRoleByName(name);
	}
	
	@Override
	public Role getRoleById(int roleId) {
		return roleRepository.getRoleById(roleId);
	}

}
