package org.covid.inventory.transform.util;

import org.modelmapper.ModelMapper;
import org.covid.inventory.entity.Role;
import org.covid.inventory.model.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleUtil {

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * This method converts Role to RoleDto
	 * 
	 * @param role
	 * @return RoleDto
	 */
	public RoleDto toDto(Role role) {
		return modelMapper.map(role, RoleDto.class);
	}

	/**
	 * This method converts RoleDto to Role
	 * 
	 * @param roleDto
	 * @return Role
	 */
	public Role toEntity(RoleDto roleDto) {
		return modelMapper.map(roleDto, Role.class);
	}
}
