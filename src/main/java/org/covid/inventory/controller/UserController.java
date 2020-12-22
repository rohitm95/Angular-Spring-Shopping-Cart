package org.covid.inventory.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.apache.commons.lang3.StringUtils;
import org.covid.inventory.config.JwtTokenUtil;
import org.covid.inventory.entity.Store;
import org.covid.inventory.entity.User;
import org.covid.inventory.model.ChangePassword;
import org.covid.inventory.model.JwtRequestDto;
import org.covid.inventory.model.JwtResponseDto;
import org.covid.inventory.model.UserDto;
import org.covid.inventory.model.UserPasswordDto;
import org.covid.inventory.service.StoreService;
import org.covid.inventory.service.impl.UserDetailsServiceImpl;
import org.covid.inventory.transform.util.ResponseMessage;
import org.covid.inventory.transform.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@Validated
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private UserUtil userUtil;

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	StoreService storeService;

	/**
	 * This is a health check api
	 * 
	 * @return String
	 */
	@GetMapping("/welcome")
	@ResponseBody
	public String welcome() {
		return "Welcome to Inventory App.";
	}

	/**
	 * This method will create an authentication token for logged in user
	 * 
	 * @param authenticationRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestDto authenticationRequest,
			HttpServletRequest request) throws Exception {
		LOGGER.info("Web Service called : /authenticate");
		String userName;
		User user = null;
		if (StringUtils.isNotBlank(authenticationRequest.getMobileNumber())) {
			user = userDetailsService.loadUserByMobileNumber(authenticationRequest.getMobileNumber());
		} else {
			userName = authenticationRequest.getUsername();
			user = userDetailsService.loadUserByMobileNumber(userName);
			if(StringUtils.isBlank(user.getPassword())) {
				LOGGER.debug("password is null in db, updating password");
				user = userDetailsService.updatePassword(user.getId(), new User(){{setPassword(authenticationRequest.getPassword());}});
			}
			authenticate(user.getUsername(), authenticationRequest.getPassword());
		}
		// final User user = userDetailsService.getUserByUsername(userName);
		UserDto userDto = userUtil.toDto(user);
		final String token = jwtTokenUtil.generateToken(userDto);

		ServletRequestAttributes attr = (ServletRequestAttributes) 
				RequestContextHolder.currentRequestAttributes();
		
		HttpSession session = attr.getRequest().getSession(true);
		session.setAttribute("USER_ID", userDto.getUsername());
		return ResponseEntity.ok(new JwtResponseDto(token, userDto));
	}

	/**
	 * This method will register a user in database
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/register", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto) throws Exception {
		LOGGER.info("Web Service called : /register");
		if (userDto.getStore().getId()==null)
		{
			Store store = storeService.findById(1);
			userDto.setStore(store);	
		}
		
		return ResponseEntity.ok(userUtil.toDto(userDetailsService.save(userUtil.toEntity(userDto))));
	}

	/**
	 * 
	 * This method gets all active users from database
	 * 
	 * @return List of users
	 */
	@RequestMapping(value = "/api/users", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
	public List<User> getAllUsers() {
		return userDetailsService.getAllUsers();
	}

	/**
	 * This method will get a user by given id
	 * 
	 * @return User
	 */
	@RequestMapping(value = "/api/users/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public User getUserById(@PathVariable @Min(1) int id) {
		return userDetailsService.getUserById(id);
	}

	/**
	 * This method will update a record of user in database
	 * 
	 * @param id
	 * @param UserDto
	 * @return UserDto
	 */
	@PutMapping("/api/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public UserDto updateUser(@PathVariable @Min(1) int id, @Valid @RequestBody UserDto userDto) {
		return userUtil.toDto(userDetailsService.updateUser(id, userUtil.toEntity(userDto)));
	}
	
	/**
	 * This method will update a record of user in database
	 * 
	 * @param id
	 * @param UserDto
	 * @return UserDto
	 */
	@PutMapping("/api/users/password/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
	public UserDto updateUserPassword(@PathVariable @Min(1) int id, @Valid @RequestBody UserPasswordDto userDto) {
		
		return userUtil.toDto(userDetailsService.updatePassword(id, userUtil.toEntity(userDto)));
	}

	/**
	 * This method will update password in database
	 * 
	 * @param id
	 * @param UserDto
	 * @return UserDto
	 */
	@PutMapping("/api/users/password/change/{id}")
	public ResponseEntity<ResponseMessage<UserDto>> changePassword(@PathVariable @Min(1) int id, @Valid @RequestBody ChangePassword userDto) {
		
		ResponseMessage<UserDto> responseMessage = new ResponseMessage<UserDto>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		if (userDto != null)
		{
			
			 String oldPassword = ((String) Optional.ofNullable(userDto.getOldPassword()).orElse(""));
	         String newpassword = ((String) Optional.ofNullable(userDto.getPassword()).orElse(""));
	         if(oldPassword.equals(newpassword)) {
	        	 responseMessage.setResult(null);
			   	  responseMessage.setStatus(400);
				  responseMessage.setStatusText("Password can't be same, please provide new password!!");
				  return new ResponseEntity<ResponseMessage<UserDto>>(responseMessage, HttpStatus.BAD_REQUEST);
	         }
			    User user=userDetailsService.changePassword(id, userUtil.toEntity(userDto),oldPassword);
				if(user!=null)
				{
					responseMessage.setResult(userUtil.toDto(user));
					responseMessage.setStatus(200);
					responseMessage.setStatusText("Password Changed Successfully!!");
					return new ResponseEntity<ResponseMessage<UserDto>>(responseMessage, HttpStatus.OK);
				 
				}else
				{ responseMessage.setResult(null);
			   	  responseMessage.setStatus(400);
				  responseMessage.setStatusText("Invalid password, please provide the valid passwords");
				  return new ResponseEntity<ResponseMessage<UserDto>>(responseMessage, HttpStatus.BAD_REQUEST);
				}
		
		}
		else
		{
			responseMessage.setResult(null);
			responseMessage.setStatus(404);
			responseMessage.setStatusText("Please provide the valid Request Body!!");
			return new ResponseEntity<ResponseMessage<UserDto>>(responseMessage, HttpStatus.OK);
		}
	}
	@PutMapping("/api/users/forgotpassword/{type}/{value}")
	public ResponseEntity<ResponseMessage<UserDto>> forgotPassword(@PathVariable String type, @PathVariable String value){
		
		ResponseMessage<UserDto> responseMessage = new ResponseMessage<UserDto>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		
		System.out.println("type-->"+type+"-Value-->"+value);
		
		if (type!=null && value!=null)
		{
			
			 if(type.equals("mobile")) 
			 {
				 
				 try
				 {
					        	
					        User user = userDetailsService.loadUserByMobileNumber(value);
							user=userDetailsService.updateForgotPassword(user);
							
							responseMessage.setResult(userUtil.toDto(user));
							responseMessage.setStatus(200);
							responseMessage.setStatusText("Password Update Successfully!!");
							return new ResponseEntity<ResponseMessage<UserDto>>(responseMessage, HttpStatus.OK);
						//user=userDetailsService.save(user);
				  }catch(Exception e)
				  {
				        	responseMessage.setResult(null);
							responseMessage.setStatus(400);
							responseMessage.setStatusText("USER NOT FOUND FOR the Given Mobile Number, Please provide valid mobile number!! ");
							return new ResponseEntity<ResponseMessage<UserDto>>(responseMessage, HttpStatus.BAD_REQUEST);
				  }
		    }
			 else if(type.equals("email"))
			 {
				    
				 try
				 {
				    User user = userDetailsService.loadUserByEmailId( value);
					user=userDetailsService.updateForgotPassword(user);
					responseMessage.setResult(userUtil.toDto(user));
					responseMessage.setStatus(200);
					responseMessage.setStatusText("Password Update Successfully!!");
					return new ResponseEntity<ResponseMessage<UserDto>>(responseMessage, HttpStatus.OK);
				 }catch(Exception e)
				  {
			        	responseMessage.setResult(null);
						responseMessage.setStatus(400);
						responseMessage.setStatusText("USER NOT FOUND FOR the Given Emaild Id, Please provide valid email Id!! ");
						return new ResponseEntity<ResponseMessage<UserDto>>(responseMessage, HttpStatus.BAD_REQUEST);
			      }
			 }
			 else {
				    responseMessage.setResult(null);
					responseMessage.setStatus(400);
					responseMessage.setStatusText("Invalid type, Please provide correct values ");
					return new ResponseEntity<ResponseMessage<UserDto>>(responseMessage, HttpStatus.BAD_REQUEST);
			 }
				 
		}
		else
		{
			responseMessage.setResult(null); 
			responseMessage.setStatus(404);
			responseMessage.setStatusText("Please provide the valid parameter ");
			return new ResponseEntity<ResponseMessage<UserDto>>(responseMessage, HttpStatus.BAD_REQUEST);
		}
	}
	/**
	 * This method is use for delete user
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/api/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUserById(@PathVariable @Min(1) int id) {
		userDetailsService.deleteUserById(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * This method authenticates the given user
	 * 
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	/*@RequestMapping(value = "/api/csv", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createUsersWithCSV(@RequestParam("file") MultipartFile file) {
		List<User> users = userUtil.readUsers(file);
		// TODO: save users in DB?
		//List<User> updatedUsers = userDetailsService.save(users);
		return ResponseEntity
				.ok(messageSource.getMessage("users.created", new Integer[] { updatedUsers.size() }, null));
	}*/
	
	/**
	 * register Users from CSV files
	 */
	@PostMapping("/api/user/upload")
	public ResponseEntity<?> createUsersFromExcel(@RequestParam("file") MultipartFile file) {
		LOGGER.info("Web Service called : /api/user/upload");
		List<User> users= null;
		List<User> savedUsersList= null;
		final String fileName = file.getOriginalFilename();
		String[] splitFileName = fileName.split("\\.");
		String firstString = splitFileName[0];
		String[] last = firstString.split("_");
		String storeName;
		if (last.length == 1)
			storeName = null;
		else
			storeName = last[last.length - 1];
		
		LOGGER.info("Inserting inventory for store");
		Store store = null;
		if (storeName != null)
			store = storeService.getStoreByName(storeName);
		else
			store = storeService.findById(1);
		
		ResponseMessage<List<User>> responseMessage = new ResponseMessage<List<User>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		try {
				users = userDetailsService.readUsersListFromFile(file);
				savedUsersList = userDetailsService.save(users, store);
				if(savedUsersList.size() > 0)
				{
					responseMessage.setResult(savedUsersList);
					responseMessage.setStatus(200);
					responseMessage.setStatusText("SUCCESS");
				}
				else
				{
					responseMessage.setResult(savedUsersList);
					responseMessage.setStatus(404);
					return new ResponseEntity<ResponseMessage<List<User>>>(responseMessage,HttpStatus.NOT_FOUND);
				}
		}catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<User>>>(responseMessage,HttpStatus.NOT_FOUND);		
		}
		return new ResponseEntity<ResponseMessage<List<User>>>(responseMessage,HttpStatus.OK);
	}
	
	/**
	 * Fetch all customers
	 */
	@GetMapping("/api/user/customers")
	public ResponseEntity<ResponseMessage<List<User>>> getCustomers(@RequestParam(value = "isActive",required = false) Boolean isActive) {
		LOGGER.info("Web Service called : /api/user/customers");
		List<User> userList = null;
		ResponseMessage<List<User>> responseMessage = new ResponseMessage<List<User>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		try {
			userList = userDetailsService.getCustomersList(isActive);
			if (userList != null && userList.size() > 0) {
				responseMessage.setResult(userList);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<User>>>(responseMessage, HttpStatus.OK);
			}

		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<User>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<User>>>(responseMessage, HttpStatus.NOT_FOUND);
	}

	/**
	 * This method will update user in database with given csv file
	 * 
	 * @param csv file
	 * @return StudentDto
	 */
	@PutMapping("/api/csv")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateUserWithCSV(@RequestParam("file") MultipartFile file) {
		List<User> users = userUtil.readUsers(file);
		// TODO: save users in DB?
		List<User> savedUsers = userDetailsService.update(users);
		return ResponseEntity.ok(messageSource.getMessage("users.updated", new Integer[] { savedUsers.size() }, null));
	}
}
