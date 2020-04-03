package edu.praveen.mobileappws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.praveen.mobileappws.service.AddressService;
import edu.praveen.mobileappws.service.UserService;
import edu.praveen.mobileappws.ui.model.reponse.AddressRest;
import edu.praveen.mobileappws.ui.model.reponse.ErrorMessages;
import edu.praveen.mobileappws.ui.model.reponse.UserRest;
import edu.praveen.mobileappws.ui.model.request.UserDetailsRequestModel;
import edu.praveen.mobileappws.ui.model.shared.dto.AddressDto;
import edu.praveen.mobileappws.ui.model.shared.dto.UserDto;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;

	@GetMapping(path = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest getUser(@PathVariable String userId) {

		System.out.println("in Get User Method :" + userId);
		UserRest returnValue = new UserRest();

		UserDto user = userService.getUserByID(userId);

		BeanUtils.copyProperties(user, returnValue);
		return returnValue;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })

	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		if (userDetails.getFirstName().isEmpty())
			throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		ModelMapper map = new ModelMapper();
		UserDto userDto = map.map(userDetails, UserDto.class);
		// BeanUtils.copyProperties(userDetails, userDto);

		UserDto createdUser = userService.createUser(userDto);

		UserRest returnValue = map.map(createdUser, UserRest.class);
		// BeanUtils.copyProperties(createdUser, returnValue);
		return returnValue;
	}

	@PutMapping(path = "/{userId}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE }

	)
	public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails)
			throws Exception {

		UserRest returnValue = new UserRest();

		if (userDetails.getFirstName().isEmpty())
			throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		userDto.setUserId(userId);

		UserDto updatedUser = userService.updateUser(userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);
		return returnValue;
	}

	@DeleteMapping(path = "/{userId}")
	public String deleteUser(@PathVariable String userId) {

		System.out.print("in delete method");
		String user = userService.deleteUser(userId);

		System.out.print("in delete method");
		return user;
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })

	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {

		List<UserRest> returnValue = new ArrayList<>();

		List<UserDto> users = userService.getUsers(page, limit);

		for (UserDto user : users) {

			UserRest userRest = new UserRest();

			BeanUtils.copyProperties(user, userRest);
			returnValue.add(userRest);
		}

		return returnValue;
	}

	@GetMapping(path = "/{userId}/addresses", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public List<AddressRest> getUserAddresses(@PathVariable String userId) {

		List<AddressRest> returnValue = new ArrayList<AddressRest>();

		List<AddressDto> addressesDto = addressService.getAddresses(userId);

		if (addressesDto != null && !addressesDto.isEmpty()) {

			Type listType = new TypeToken<List<AddressRest>>() {
			}.getType();
			ModelMapper map = new ModelMapper();
			returnValue = map.map(addressesDto, listType);

		}
		return returnValue;
	}

	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public AddressRest getUserAddress( @PathVariable String addressId) {

		

	AddressDto addressesDto = addressService.getAddress(addressId);

			ModelMapper map = new ModelMapper();
			
		
		return map.map(addressesDto, AddressRest.class);
	}
}
