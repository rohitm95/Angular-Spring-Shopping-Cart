package org.covid.inventory.service.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.covid.inventory.datajpa.RoleRepository;
import org.covid.inventory.entity.Role;
import org.covid.inventory.exceptions.EntityNotFoundException;
import org.covid.inventory.service.impl.RoleServiceImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceImplTest {

	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	RoleServiceImpl roleServiceImpl;

	Role role;

	@Before
	public void init() {
		role = MockData.getRole(1);
	}

	@Test
	public void testGetAllRoles() {
		Mockito.when(roleRepository.findAll()).thenReturn(Arrays.asList(role));
		List<Role> res = roleServiceImpl.getAllRoles();
		assertNotNull(res);
		assertEquals(1, res.size());
	}

	@Test
	public void testGetRoleByName() {
		Mockito.when(roleRepository.getRoleByName(Mockito.anyString())).thenReturn(role);
		Role res = roleServiceImpl.getRoleByName(Mockito.anyString());
		assertNotNull(res);
	}
	
}
