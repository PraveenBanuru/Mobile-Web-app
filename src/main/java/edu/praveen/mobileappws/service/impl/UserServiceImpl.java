package edu.praveen.mobileappws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.praveen.mobileappws.io.entity.UserEntity;
import edu.praveen.mobileappws.repository.UserRepository;
import edu.praveen.mobileappws.service.UserService;
import edu.praveen.mobileappws.ui.model.shared.Utils;
import edu.praveen.mobileappws.ui.model.shared.dto.AddressDto;
import edu.praveen.mobileappws.ui.model.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	Utils utils;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {

		UserEntity storedUserDetails = userRepo.findUserByEmail(user.getEmail());
		
		for(int i=0;i<user.getAddresses().size();i++) {
			
			AddressDto address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
			
			user.getAddresses().set(i, address);
			
		}
		
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity= modelMapper.map(user, UserEntity.class);
		//BeanUtils.copyProperties(user, userEntity);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		
			
		if (storedUserDetails == null) {
			storedUserDetails = userRepo.save(userEntity);
		} else
			throw new RuntimeException("Record alredy exists");
		
		
		UserDto returnValue= modelMapper.map(storedUserDetails, UserDto.class);
		//BeanUtils.copyProperties(storedUserDetails, returnValue);

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UserEntity userEntity = userRepo.findUserByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email + "not found");
		return new User(email, userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	public UserDto getUser(String email) {

		UserEntity userEntity = userRepo.findUserByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email + "not found");

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);

		return returnValue;
	}

	@Override
	public UserDto getUserByID(String userId) {

		UserEntity userEntity = userRepo.findByUserId(userId);

		if (userEntity == null)
			throw new UsernameNotFoundException(userId + "not found");

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);

		return returnValue;
	}

	@Override
	public UserDto updateUser(UserDto user) {

		UserEntity userDetails = userRepo.findByUserId(user.getUserId());

		if (userDetails != null)
			System.out.println(userDetails.getEmail());

		userDetails.setFirstName(user.getFirstName());
		userDetails.setLastName(user.getLastName());

		UserEntity storedUser = userRepo.save(userDetails);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUser, returnValue);
		return returnValue;
	}

	@Override
	public String deleteUser(String userId) {

		UserEntity userDetails = userRepo.findByUserId(userId);

		if (userDetails == null)
			throw new UsernameNotFoundException(userId + "not found");

		userRepo.delete(userDetails);

		return "User Deleted";
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {

		List<UserDto> returnValue = new ArrayList<>();

		Pageable pageable = PageRequest.of(page, limit);
		Page<UserEntity> pages = userRepo.findAll(pageable);
		List<UserEntity> users = pages.getContent();

		for (UserEntity user : users) {

			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			returnValue.add(userDto);
		}

		return returnValue;
	}
}
