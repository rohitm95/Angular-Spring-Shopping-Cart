package org.covid.inventory.transform.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.covid.inventory.entity.Role;
import org.covid.inventory.entity.User;
import org.covid.inventory.exceptions.BadRequestException;
import org.covid.inventory.exceptions.ServiceException;
import org.covid.inventory.exceptions.UniqueConstraintException;
import org.covid.inventory.model.ChangePassword;
import org.covid.inventory.model.UserDto;
import org.covid.inventory.model.UserPasswordDto;
import org.covid.inventory.service.RoleService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.exceptions.CsvBeanIntrospectionException;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

@Component
public class UserUtil extends ColumnPositionMappingStrategy<UserDto> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserUtil.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	RoleUtil roleUtil;

	@Autowired
	private MessageSource messageSource;

	public UserUtil() {
		this.setType(UserDto.class);
	}

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * This method converts User to UserDto
	 * 
	 * @param user
	 * @return UserDto
	 */
	public UserDto toDto(User user) {
		/*
		 * PropertyMap<User, UserDto> userMap = new PropertyMap<User, UserDto>() {
		 * protected void configure() { map().setRole(new
		 * RoleUtil().toDto(source.getRole())); } };
		 * 
		 * System.out.println(modelMapper); modelMapper.addMappings(userMap);
		 */
		return modelMapper.map(user, UserDto.class);
	}

	/**
	 * This method converts UserDto to User
	 * 
	 * @param userDto
	 * @return User
	 */
	public User toEntity(UserDto userDto) {
		return modelMapper.map(userDto, User.class);
	}

	/**
	 * This method converts UserPasswordDto to User
	 * 
	 * @param userDto
	 * @return User
	 */
	public User toEntity(UserPasswordDto userDto) {
		return modelMapper.map(userDto, User.class);
	}
	
	/**
	 * This method converts ChangePassword to User
	 * 
	 * @param userDto
	 * @return User
	 */
	public User toEntity(ChangePassword userDto) {
		return modelMapper.map(userDto, User.class);
	}
	/**
	 *
	 * @param entityList list of entities that needs to be mapped
	 */
	public List<User> mapAllToEntity(final Collection<UserDto> userDtoList) {
		return userDtoList.stream().map(entity -> toEntity(entity)).collect(Collectors.toList());
	}
	
	/**
	 *
	 * @param entityList list of entities that needs to be mapped
	 */
	public List<UserDto> mapAllToUserDtoList(final Collection<User> users) {
		return users.stream().map(dto -> toDto(dto)).collect(Collectors.toList());
	}

	/**
	 * Maps bean to the csv file inputs for User resource
	 * 
	 * @author akshata_gaonkar
	 *
	 */
	@Override
	public UserDto populateNewBean(String[] line) throws CsvBeanIntrospectionException, CsvRequiredFieldEmptyException,
			CsvDataTypeMismatchException, CsvConstraintViolationException, CsvValidationException {
		UserDto userDto = new UserDto();
		Integer counter = 0;
		userDto.setFirstName(line[counter++]);
		userDto.setLastName(line[counter++]);
		userDto.setEmailId(line[counter++]);
		userDto.setGender(line[counter++]);
		userDto.setDateOfJoin(line[counter++]);
		userDto.setActive(Boolean.valueOf(line[counter++]));
		userDto.setUsername(line[counter++]);
		userDto.setPassword(line[counter++]);
		Role role = roleService.getRoleByName(line[counter]);
		if (role == null) {
			LOGGER.debug("Role invalid: " + line[counter]);
			throw new CsvValidationException(
					messageSource.getMessage("role.not.found", new String[] { line[counter] }, null));
		}
		userDto.setRole(roleUtil.toDto(role));
		return userDto;
	}

	/*
	 * Reads list of users from given csv file
	 */
	public List<User> readUsers(MultipartFile file) {
		List<User> users = null;
		// validate file
		if (file.isEmpty()) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), "csv.file.empty", null);
		} else {
			// parse CSV file to create a list of User objects
			Reader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				// create csv bean reader
				CsvToBean<UserDto> userCsvToBean = new CsvToBeanBuilder<UserDto>(reader).withType(UserDto.class)
						.withSkipLines(1).withMappingStrategy(this).build();

				// set filter to skip blank line
				userCsvToBean.setFilter(new CsvToBeanFilter() {
					@Override
					public boolean allowLine(String[] line) {
						boolean blankLine = line.length == 1 && line[0].isEmpty();
						return !blankLine;
					}
				});

				// Check for duplicates in csv
				List<UserDto> usersDto = userCsvToBean.parse();
				List<String> userNames = usersDto.stream().map(UserDto::getUsername).collect(Collectors.toList());
				List<String> emails = usersDto.stream().map(UserDto::getEmailId).collect(Collectors.toList());
				userNames.addAll(emails);
				List<String> duplicates = userNames.stream().filter(n -> Collections.frequency(userNames, n) > 1)
						.distinct().collect(Collectors.toList());
				if (!duplicates.isEmpty()) {
					LOGGER.debug("Duplicates found in csv : " + duplicates);
					throw new ConstraintViolationException(
							messageSource.getMessage("duplicate.username.or.email", null, null) + StringUtils.SPACE
									+ String.join(", ", duplicates),
							null);
				}

				// Validate beans
				ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
				Validator validator = factory.getValidator();
				StringBuilder validations = new StringBuilder();
				int count = 1;
				for (UserDto userDto : usersDto) {
					Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
					if (!(violations == null || violations.isEmpty())) {
						validations.append("Row " + (count) + " : " + getViolationMessage(violations)+ "\n ");
					}
					count++;
				}

				if (validations.length() != 0) {
					throw new ConstraintViolationException(
							validations.deleteCharAt(validations.length() - 2).toString(), null);
				}

				// convert `CsvToBean` object to list of users and DTO to Entity
				users = mapAllToEntity(usersDto);
			} catch (UniqueConstraintException ex) {
				LOGGER.debug("Unique constraint exception: " + ex.getCause());
				throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.csv.processing", null);
			} catch (IOException ex) {
				LOGGER.debug("IO exception: " + ex.getCause());
				throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.csv.processing", null);
			} catch (ConstraintViolationException ex) {
				LOGGER.debug("Constraint Violation Exception: " + ex.getCause());
				throw ex;
			} catch (Exception ex) {
				LOGGER.debug("Exception occurred while parsing : " + ex.getCause());
				if (ex.getCause() != null && (ex.getCause() instanceof CsvValidationException)) {
					CsvValidationException csvEx = (CsvValidationException) ex.getCause();
					throw new ConstraintViolationException(
							"Row " + (csvEx.getLineNumber() - 1) + ": " + csvEx.getMessage(), null);
				} else {
					throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.csv.processing",
							new String[] { ex.getCause().toString() });
				}
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						LOGGER.debug("Exception whileclosing the reader: " + e.getCause());
						throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.csv.processing",
								null);
					}
				}
			}
		}
		return users;
	}

	private String getViolationMessage(Set<ConstraintViolation<UserDto>> violations) {
		StringBuilder msg = new StringBuilder();
		for (ConstraintViolation<UserDto> constraintViolation : violations) {
			msg.append(messageSource
					.getMessage(constraintViolation.getMessageTemplate().replaceAll("[{}]", ""), null, null)
					.replace(".", ""));
			msg.append(", ");
		}

		return msg.toString();
	}
}
