package org.covid.inventory.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.covid.inventory.datajpa.UserRepository;
import org.covid.inventory.entity.Role;
import org.covid.inventory.entity.Store;
import org.covid.inventory.entity.User;
import org.covid.inventory.exceptions.EntityNotFoundException;
import org.covid.inventory.exceptions.UniqueConstraintException;
import org.covid.inventory.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 

@Service
public class UserDetailsServiceImpl extends GenericServiceImpl implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findActiveUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username, null);
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				getAuthority(user));
	}

	public User loadUserByEmailId(String emailId) throws UsernameNotFoundException {
		User user = userRepository.findActiveUserByEmailId(emailId);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with emailId: " + emailId, null);
		}
		return user;
	}
	
	public User loadUserByMobileNumber(String mobileNumber) throws UsernameNotFoundException {
		User user = userRepository.findActiveUserByMobileNumber(mobileNumber);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with emailId: " + mobileNumber, null);
		}
		return user;
	}

	public User getActiveUserByUsername(String username) throws UsernameNotFoundException {
		// getting user from database
		return userRepository.findActiveUserByUsername(username);
	}
	
	public User getActiveUserByMobileNumber(String mobileNumber) throws UsernameNotFoundException {
		// getting user from database
		return userRepository.findActiveUserByMobileNumber(mobileNumber);
	}

	public User getActiveUserByEmailId(String emailId) throws UsernameNotFoundException {
		// getting user from database
		return userRepository.findActiveUserByEmailId(emailId);
	}

	private Set<? extends GrantedAuthority> getAuthority(User user) {
		LOGGER.info("user : ", user);
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase()));
		return authorities;
	}

	/**
	 * Method for get all user data
	 * 
	 * @return
	 */
	public List<User> getAllUsers() {
		return userRepository.getAllActiveUsers();
	}

	public User getUserById(int id) {
		return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND.value(),
				messageSource.getMessage("user.not.found", new Integer[] { id }, null)));
	}

	/**
	 * Method for update user data
	 * 
	 * @param user
	 * @return
	 */
	public User updateUser(int id, User updateUser) {
		User user = getUserById(id);
        String password = user.getPassword();
        if(updateUser.getPassword()==null)
            updateUser.setPassword(user.getPassword());
        user = updateValues(user, updateUser);
        user.setPassword(password);
        return userRepository.save(user);
	}
	
	/**
	 * Method for update password
	 * 
	 * @param user
	 * @return
	 */
	public User updatePassword(int id, User updateUser) {
		User user = getUserById(id);
		
		user.setPassword(bcryptEncoder.encode(updateUser.getPassword()));
		return userRepository.save(user);
	}

	/**
	 * Method for change password
	 * 
	 * @param user
	 * @return
	 */
	public User changePassword(int id, User updateUser,String oldPassword) {
		User user = getUserById(id);
		try {
			    
			    LOGGER.debug("Updating password with new password");
			    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), oldPassword));
				user.setPassword(bcryptEncoder.encode(updateUser.getPassword()));
				user=userRepository.save(user);
				LOGGER.debug("Password updated successfully for the user:-"+user.getUsername());
			
		}catch (Exception e) {
			// TODO: handle exception
			
			LOGGER.error("Invalid password error occured"+e);
			return null;
		}
		
		
		
		return user;
	}
	
	public User updateForgotPassword(User user)

	{
		user.setPassword(null);
		return userRepository.save(user);
	}
	
	/**
	 * Method for update user data
	 * 
	 * @param user
	 * @return
	 */
	public User updateUser(String username, User updateUser) {
		User user = userRepository.findUserByUsername(username);
		user = updateValues(user, updateUser);
		return userRepository.save(user);
	}

	/**
	 * Method for delete user by Id
	 * 
	 * @param id
	 */
	public void deleteUserById(Integer id) {
		if (!userRepository.existsById(id)) {
			throw new EntityNotFoundException(HttpStatus.NOT_FOUND.value(),
					messageSource.getMessage("user.not.found", new Integer[] { id }, null));
		}
		userRepository.softDeleteUserById(id);
	}

	/**
	 * Method for save/add user in database
	 * 
	 * @param user
	 * @return
	 */
	public User save(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new UniqueConstraintException(HttpStatus.NOT_FOUND.value(),
					messageSource.getMessage("duplicate.username", new String[] { user.getUsername() }, null));
		} else if (userRepository.existsByEmailId(user.getEmailId())) {
			throw new UniqueConstraintException(HttpStatus.NOT_FOUND.value(),
					messageSource.getMessage("duplicate.emailid", new String[] { user.getEmailId() }, null));
		}
		return userRepository.save(user);
	}

	/**
	 * Method for save/add user in database
	 * 
	 * @param users
	 * @return list of created users
	 */
	public List<User> save(List<User> users, Store store) {
		List<User> savedUsersList = new ArrayList<User>();
		try {
			List<User> userDataExisting = userRepository.getAllUsers();
			Map<String, User> userDataMapExisting = new LinkedHashMap<>();
			for(User data : userDataExisting) {
				userDataMapExisting.put(data.getUsername(),data);
			}
			Map<String, User> userData = new LinkedHashMap<>();
			for(User data : users) {
				User user = new User();
				user.setUsername(data.getUsername());
				user.setMobileNumber(data.getMobileNumber());
				user.setFirstName(data.getFirstName());
				user.setEmailId(data.getEmailId());
				Role role = roleService.getRoleById(data.getRole().getId());
				user.setRole(role);
				user.setIsActive(data.getIsActive());
				user.setCategory(data.getCategory());
				user.setAFD_purchase_limit(data.getAFD_purchase_limit());
				user.setNonAFD_purchase_limit(data.getNonAFD_purchase_limit());
				user.setStore(store);
				userData.put(data.getUsername(),user);
			}
			List<User> userList = new ArrayList<>();
			
			for (Map.Entry<String, User> entry : userData.entrySet()) {
				if(userDataMapExisting.containsKey(entry.getKey())){
					
					User user = userDataMapExisting.get(entry.getKey());
					Role role = roleService.getRoleById(entry.getValue().getRole().getId());
					user.setRole(role);
					user.setUsername(entry.getValue().getUsername());
					user.setMobileNumber(entry.getValue().getMobileNumber());
					user.setFirstName(entry.getValue().getFirstName());
					user.setEmailId(entry.getValue().getEmailId());
					user.setIsActive(entry.getValue().getIsActive());
					user.setCategory(entry.getValue().getCategory());
					user.setAFD_purchase_limit(entry.getValue().getAFD_purchase_limit());
					user.setNonAFD_purchase_limit(entry.getValue().getNonAFD_purchase_limit());
					user.setStore(store);
					userList.add(user);
				}else {
					userList.add(entry.getValue());
				}
			}
			savedUsersList = userRepository.saveAll(userList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savedUsersList;
	}

	/**
	 * Method for bulk update users in database
	 * 
	 * @param users
	 * @return list of updated users
	 */
	public List<User> update(List<User> updateUsers) {
		StringBuilder usernameMessage = new StringBuilder(messageSource.getMessage("user.not.found.for.records", null, null));
		StringBuilder username = new StringBuilder();
		StringBuilder emailMessage = new StringBuilder(messageSource.getMessage("duplicate.emailid.for.records", null, null));
		StringBuilder email = new StringBuilder();
		List<User> users = new ArrayList<User>();

		updateUsers.forEach(user -> {
			// check if username is exist for user to be updated
			if (!userRepository.existsByUsername(user.getUsername())) {
				username.append(user.getUsername());
				username.append(", ");
			} else {
				// check if emailid is exist for other user
				User tempUser = userRepository.findUserByEmailId(user.getEmailId());
				if (tempUser != null && !tempUser.getUsername().equals(user.getUsername())) {
					email.append(user.getEmailId());
					email.append(", ");
				}
			}
		});
		if (!username.toString().isEmpty()) {
			usernameMessage = usernameMessage.append(username.toString()).deleteCharAt(usernameMessage.length() - 2);
			throw new EntityNotFoundException(HttpStatus.NOT_FOUND.value(), usernameMessage.toString());
		} else if (!email.toString().isEmpty()) {
			emailMessage = emailMessage.append(email.toString()).deleteCharAt(emailMessage.length() - 2);
			throw new UniqueConstraintException(HttpStatus.BAD_REQUEST.value(), emailMessage.toString());
		} else {
			for (User updateUser : updateUsers) {
				User existingUser = userRepository.findUserByUsername(updateUser.getUsername());
				existingUser = updateValues(existingUser, updateUser);
				users.add(existingUser);
			}
		}
		return userRepository.saveAll(users);
	}

	private User updateValues(User user, User updateUser) {
		user.setFirstName(updateUser.getFirstName());
		user.setLastName(updateUser.getLastName());
		user.setEmailId(updateUser.getEmailId());
		user.setMobileNumber(updateUser.getMobileNumber());
		user.setGender(updateUser.getGender());
		user.setIsActive(updateUser.getIsActive());
		if(updateUser.getPassword()!=null)
		user.setPassword(bcryptEncoder.encode(updateUser.getPassword()));
		user.setRole(updateUser.getRole());
		user.setAFD_purchase_limit(updateUser.getAFD_purchase_limit());
		user.setNonAFD_purchase_limit(updateUser.getNonAFD_purchase_limit());
		user.setCategory(updateUser.getCategory());
		return user;
	}
	
	private static boolean isRowEmpty(Row row) {
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();

		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
					isEmpty = false;
					break;
				}
			}
		}

		return isEmpty;
	}
	
	@SuppressWarnings({ "resource" })
	public List<User> readUsersListFromFile(MultipartFile file) throws Exception {
		LOGGER.debug("in readUsersListFromFile()....");
		List<User> users = null;
		String SHEET = "Sheet1";
		try {
			users = new ArrayList<User>();

			Workbook workbook = new XSSFWorkbook(file.getInputStream());
		     
			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}
				
				if (isRowEmpty(currentRow)) {
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				User user = new User();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					switch (cellIdx) {
					case 0:
						user.setUsername(currentCell.getStringCellValue());
						break;
						
					case 1:
						user.setMobileNumber(currentCell.getStringCellValue());
						break;	

					case 2:
						user.setFirstName(currentCell.getStringCellValue());
						break;

					case 3:
						Role role = roleService.getRoleById((int)(currentCell.getNumericCellValue()));
						if (role == null) {
							LOGGER.debug("Role invalid: " + currentCell.getNumericCellValue());
							throw new Exception(); 
						}
						else
							user.setRole(role);
						break;

					case 4:
						String activeFlag = currentCell.getStringCellValue();
						if(activeFlag.equals("Activate"))
							user.setIsActive(true);
						else if(activeFlag.equals("Deactivate"))
							user.setIsActive(false);
						break;
						
					case 5:
						user.setCategory(currentCell.getStringCellValue());
						break;

					case 6:
						Double AFDLimit = currentCell.getNumericCellValue();
						user.setAFD_purchase_limit(AFDLimit);
						break;
						
					case 7:
						Double NonAFDLimit = currentCell.getNumericCellValue();
						user.setNonAFD_purchase_limit(NonAFDLimit);
						break;
						
					case 8:
						user.setEmailId(currentCell.getStringCellValue());
						break;	

					default:
						break;
					}

					cellIdx++;
				}
				users.add(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage()); 
		}
		return users;
	}
	
	public List<User> getCustomersList(Boolean isActive) {
		List<User> users = null;
		
		if(isActive==null)
			users = userRepository.getAllCustomers();
		else
			users=userRepository.getAllActiveOrInactiveCustomers(isActive);
		
		return users;
	}
}
