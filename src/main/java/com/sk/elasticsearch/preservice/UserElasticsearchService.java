package com.sk.elasticsearch.preservice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sk.elasticsearch.entity.UserElasticsearch;
import com.sk.elasticsearch.repository.UserElasticsearchRepo;
import com.sk.elasticsearch.service.IndexService;
import com.sk.elasticserach.helper.Indices;
import com.sk.entity.User;
import com.sk.exceptions.ResourceNotFoundException;
import com.sk.payloads.UserDto;

@Service
public class UserElasticsearchService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserElasticsearchRepo repo;

	@Autowired
	private IndexService service;

	public void insertElasticUser(User user) {

		boolean b = service.isIndexAvailableOrNot(Indices.USER_INDEX);

		UserElasticsearch euser = new UserElasticsearch();
		UserElasticsearch ui = this.modelMapper.map(user, UserElasticsearch.class);

		repo.save(ui);
	}

	public UserDto updateElasticUser(UserDto user, Integer userId) {

		UserElasticsearch euser = this.repo.findById(userId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
		euser.setName(user.getName());
		euser.setEmail(user.getEmail());
		euser.setPassword(user.getPassword());
		euser.setAbout(user.getAbout());
		UserElasticsearch userUpdate = this.repo.save(euser);
		UserDto ui = this.modelMapper.map(userUpdate, UserDto.class);
		return ui;

	}

	public UserDto getEUserById(Integer userId) {

		UserElasticsearch user = this.repo.findById(userId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
		UserDto ui = this.modelMapper.map(user, UserDto.class);

		return ui;
	}

	public List<UserDto> getEAllUsers() {

		List<UserElasticsearch> list = new ArrayList<UserElasticsearch>();
		Iterable<UserElasticsearch> itr = repo.findAll();

		for (UserElasticsearch l : itr) {
			list.add(l);
		}

		List<UserDto> luserDto = list.stream().map((use) -> this.modelMapper.map(use, UserDto.class))
				.collect(Collectors.toList());

		return luserDto;
	}

	public void deleteEUser(Integer userId) {
		UserElasticsearch user = this.repo.findById(userId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
		repo.deleteById(user.getId());

	}
}