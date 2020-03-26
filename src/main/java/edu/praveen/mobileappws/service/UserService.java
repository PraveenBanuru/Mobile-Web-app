package edu.praveen.mobileappws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import edu.praveen.mobileappws.ui.model.shared.dto.UserDto;

public interface UserService extends UserDetailsService{
	
	UserDto	createUser(UserDto user);
	UserDto getUser(String email);
	UserDto getUserByID(String userID);
	UserDto updateUser(UserDto user);
	String deleteUser(String userID);
    List<UserDto> getUsers(int page, int limit);
}
