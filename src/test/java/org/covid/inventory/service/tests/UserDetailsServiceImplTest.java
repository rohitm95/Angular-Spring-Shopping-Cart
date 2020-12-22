//package org.covid.inventory.service.tests;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.covid.inventory.datajpa.UserRepository;
//import org.covid.inventory.entity.User;
//import org.covid.inventory.exceptions.EntityNotFoundException;
//import org.covid.inventory.exceptions.UniqueConstraintException;
//import org.covid.inventory.service.impl.UserDetailsServiceImpl;
//import org.springframework.context.MessageSource;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@RunWith(MockitoJUnitRunner.class)
//public class UserDetailsServiceImplTest {
//
//	@Mock
//	private UserRepository userRepository;
//
//	@Mock
//	private PasswordEncoder bcryptEncoder;
//
//	@Mock
//	MessageSource messageSource;
//
//	@InjectMocks
//	UserDetailsServiceImpl userDetailsServiceImpl;
//
//	User user;
//
//	@Before
//	public void init() {
//		user = MockData.getUser(1);
//	}
//
//	@Test
//	public void testLoadUserByMobileNumber() {
//		Mockito.when(userRepository.findActiveUserByMobileNumber(Mockito.anyString())).thenReturn(user);
//		UserDetails res = userDetailsServiceImpl.loadUserByUsername(Mockito.anyString());
//		assertNotNull(res);
//		res.getAuthorities().forEach(i -> {
//			int resp = i.getAuthority().compareTo("ROLE_TEST");
//			assertEquals(0, resp);
//		});
//	}
//
//	@Test
//	public void testLoadUserByMobileNumberUserNotFound() {
//		try {
//			Mockito.when(userRepository.findActiveUserByMobileNumber(Mockito.anyString())).thenReturn(null);
//			userDetailsServiceImpl.loadUserByUsername(Mockito.anyString());
//		} catch (Exception e) {
//			assertNotNull(e);
//			assertTrue(e instanceof UsernameNotFoundException);
//		}
//	}
//
//	@Test
//	public void testLoadUserByEmailId() {
//		Mockito.when(userRepository.findActiveUserByEmailId(Mockito.anyString())).thenReturn(user);
//		User res = userDetailsServiceImpl.loadUserByEmailId(Mockito.anyString());
//		assertNotNull(res);
//	}
//
//	@Test
//	public void testLoadUserByEmailIdUserNotFound() {
//		try {
//			Mockito.when(userRepository.findActiveUserByEmailId(Mockito.anyString())).thenReturn(null);
//			userDetailsServiceImpl.loadUserByEmailId(Mockito.anyString());
//		} catch (Exception e) {
//			assertNotNull(e);
//			assertTrue(e instanceof UsernameNotFoundException);
//		}
//	}
//
//	@Test
//	public void testActiveUserByUsername() {
//		Mockito.when(userRepository.findActiveUserByUsername(Mockito.anyString())).thenReturn(user);
//		User res = userDetailsServiceImpl.getActiveUserByUsername(Mockito.anyString());
//		assertNotNull(res);
//	}
//
//	@Test
//	public void testActiveUserByEmailId() {
//		Mockito.when(userRepository.findActiveUserByEmailId(Mockito.anyString())).thenReturn(user);
//		User res = userDetailsServiceImpl.getActiveUserByEmailId(Mockito.anyString());
//		assertNotNull(res);
//	}
//
//	@Test
//	public void testGetAllUsers() {
//		Mockito.when(userRepository.getAllActiveUsers()).thenReturn(new ArrayList<User>(Arrays.asList(user)));
//		List<User> res = userDetailsServiceImpl.getAllUsers();
//		assertNotNull(res);
//		assertEquals(1, res.size());
//	}
//
//	@Test
//	public void testGetUserById() {
//		Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
//		User res = userDetailsServiceImpl.getUserById(Mockito.anyInt());
//		assertNotNull(res);
//	}
//
//	@Test
//	public void testUpdateUserById() {
//		Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
//		Mockito.when(bcryptEncoder.encode(Mockito.anyString())).thenReturn("test");
//		Mockito.when(userRepository.save(user)).thenReturn(user);
//		User res = userDetailsServiceImpl.updateUser(Mockito.anyInt(), user);
//		assertNotNull(res);
//	}
//
//	@Test
//	public void testUpdateUserByUsername() {
//		Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(user);
//		Mockito.when(bcryptEncoder.encode(Mockito.anyString())).thenReturn("test");
//		Mockito.when(userRepository.save(user)).thenReturn(user);
//		User res = userDetailsServiceImpl.updateUser(Mockito.anyString(), user);
//		assertNotNull(res);
//	}
//
//	@Test
//	public void testDeleteUserById() {
//		Mockito.when(userRepository.existsById(Mockito.anyInt())).thenReturn(true);
//		userDetailsServiceImpl.deleteUserById(1);
//		Mockito.verify(userRepository, Mockito.times(1)).softDeleteUserById(Mockito.anyInt());
//	}
//
//	@Test
//	public void testDeleteUserByIdNotFound() {
//		try {
//			Mockito.when(userRepository.existsById(Mockito.anyInt())).thenReturn(false);
//			Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
//					.thenReturn("Assessment not found");
//			userDetailsServiceImpl.deleteUserById(1);
//		} catch (Exception e) {
//			assertNotNull(e);
//			assertTrue(e instanceof EntityNotFoundException);
//		}
//	}
//
//	@Test
//	public void testSaveUser() {
//		Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
//		Mockito.when(bcryptEncoder.encode(Mockito.anyString())).thenReturn("test");
//		Mockito.when(userRepository.save(user)).thenReturn(user);
//		User res = userDetailsServiceImpl.save(user);
//		assertNotNull(res);
//	}
//
//	@Test
//	public void testSaveUserExistsUsername() {
//		try {
//			Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);
//			Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
//					.thenReturn("duplicate.username");
//			userDetailsServiceImpl.save(user);
//		} catch (Exception e) {
//			assertNotNull(e);
//			assertTrue(e instanceof UniqueConstraintException);
//			assertEquals("duplicate.username", e.getMessage());
//		}
//	}
//
//	@Test
//	public void testSaveUserExistsEmail() {
//		try {
//			// Mockito.when(userRepository.existsByEmailId(Mockito.anyString())).thenReturn(true);
//			/*
//			 * Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(),
//			 * Mockito.any())) .thenReturn("duplicate.emailid");
//			 */
//			userDetailsServiceImpl.save(user);
//		} catch (Exception e) {
//			assertNotNull(e);
//			assertTrue(e instanceof UniqueConstraintException);
//			assertEquals("duplicate.emailid", e.getMessage());
//		}
//	}
//
//	@Test
//	public void testSaveUserList() {
//		Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
//				.thenReturn("duplicate:");
//		Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
//		// Mockito.when(userRepository.existsByEmailId(Mockito.anyString())).thenReturn(false);
//		Mockito.when(bcryptEncoder.encode(Mockito.anyString())).thenReturn("test");
//		Mockito.when(userRepository.saveAll(Mockito.anyList())).thenReturn(Arrays.asList(user));
//		List<User> res = userDetailsServiceImpl.save(Arrays.asList(user));
//		assertNotNull(res);
//		assertEquals(1, res.size());
//	}
//
//	@Test
//	public void testSaveUserListExistsUsername() {
//		try {
//			Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
//					.thenReturn("duplicate:");
//			Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);
//			userDetailsServiceImpl.save(Arrays.asList(user));
//		} catch (Exception e) {
//			assertNotNull(e);
//			assertTrue(e instanceof UniqueConstraintException);
//			assertEquals("duplicate:test ", e.getMessage());
//		}
//	}
//
//	@Test
//	public void testSaveUserListExistsEmail() {
//		try {
//			Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
//					.thenReturn("duplicate:");
//			// Mockito.when(userRepository.existsByEmailId(Mockito.anyString())).thenReturn(true);
//			userDetailsServiceImpl.save(Arrays.asList(user));
//		} catch (Exception e) {
//			assertNotNull(e);
//			assertTrue(e instanceof UniqueConstraintException);
//			assertEquals("duplicate:test ", e.getMessage());
//		}
//	}
//
//	@Test
//	public void testUpdateUsers() {
//		Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
//				.thenReturn("duplicate");
//		Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);
//		Mockito.when(userRepository.findUserByEmailId(Mockito.anyString())).thenReturn(null);
//		Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(user);
//		Mockito.when(bcryptEncoder.encode(Mockito.anyString())).thenReturn("test");
//		Mockito.when(userRepository.saveAll(Mockito.anyList())).thenReturn(Arrays.asList(user));
//		List<User> res = userDetailsServiceImpl.update(Arrays.asList(user));
//		assertNotNull(res);
//		assertEquals(1, res.size());
//	}
//
//	@Test
//	public void testUpdateUsersUserNotFound() {
//		try {
//			Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
//					.thenReturn("user not foud:");
//			Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
//			userDetailsServiceImpl.update(Arrays.asList(user));
//		} catch (Exception e) {
//			assertNotNull(e);
//			assertTrue(e instanceof EntityNotFoundException);
//			assertEquals("user not foud:test ", e.getMessage());
//			System.out.println(e.getMessage());
//		}
//	}
//
//	@Test
//	public void testUpdateUsersEmailIdAlreadyExists() {
//		try {
//			Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
//					.thenReturn("duplicate email:");
//			Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);
//			Mockito.when(userRepository.findUserByEmailId(Mockito.anyString())).thenReturn(new User() {
//				{
//					setUsername("user1");
//				}
//			});
//			userDetailsServiceImpl.update(Arrays.asList(user));
//		} catch (Exception e) {
//			assertNotNull(e);
//			assertTrue(e instanceof UniqueConstraintException);
//			assertEquals("duplicate email:email ", e.getMessage());
//			System.out.println(e.getMessage());
//		}
//	}
//}
