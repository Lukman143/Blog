package com.sk.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sk.entity.User;
import com.sk.exceptions.ResourceNotFoundException;
import com.sk.payloads.UserDto;
import com.sk.repository.UserRepository;
import com.sk.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDto createUser(UserDto userDto) {

		User user = this.dtoToUser(userDto);

		User userSave = this.userRepo.save(user);

		return this.userToDto(userSave);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		User userUpdate = this.userRepo.save(user);
		return this.userToDto(userUpdate);
	}

	@Override
	public UserDto getUserById(Integer userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {

		List<User> luser = this.userRepo.findAll();

		// Converting list of user into list of userdto
		List<UserDto> luserDto = luser.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

		return luserDto;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
		this.userRepo.delete(user);
	}

	// convert UserDto class to User class
	/*
	 * private User dtoToUser(UserDto userDto) {
	 * 
	 * User user = new User(); user.setId(userDto.getId());
	 * user.setName(userDto.getName()); user.setEmail(userDto.getEmail());
	 * user.setPassword(userDto.getPassword()); user.setAbout(userDto.getAbout());
	 * 
	 * return user;
	 * 
	 * }
	 */

	// convert User class to UserDto class
	/*
	 * private UserDto userToDto(User user) {
	 * 
	 * UserDto userDto = new UserDto(); userDto.setId(user.getId());
	 * userDto.setName(user.getName()); userDto.setEmail(user.getEmail());
	 * userDto.setPassword(user.getPassword()); userDto.setAbout(user.getAbout());
	 * 
	 * return userDto;
	 * 
	 * }
	 */

	// convert UserDto class to User class
	public User dtoToUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		return user;
	}

	// convert User class to UserDto class
	public UserDto userToDto(User user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		return userDto;
	}

}
