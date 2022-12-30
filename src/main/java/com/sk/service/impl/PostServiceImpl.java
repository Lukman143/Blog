package com.sk.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sk.elasticsearch.preservice.PostElasticsearchService;
import com.sk.entity.Category;
import com.sk.entity.Post;
import com.sk.entity.User;
import com.sk.exceptions.ResourceNotFoundException;
import com.sk.payloads.CategoryDto;
import com.sk.payloads.CommentDto;
import com.sk.payloads.PostDto;
import com.sk.payloads.PostResponse;
import com.sk.payloads.UserDto;
import com.sk.repository.CategoryRepository;
import com.sk.repository.PostRepo;
import com.sk.repository.UserRepository;
import com.sk.service.PostService;

@Service
public class PostServiceImpl implements PostService {
	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private PostElasticsearchService esService;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id ", categoryId));

		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post newPost = this.postRepo.save(post);

		postDto.setAddedDate(new Date().toString());
		postDto.setImageName("default.png");
		PostDto eDto = esService.createEsPost(postDto, userId, categoryId);
		// return this.modelMapper.map(newPost, PostDto.class);
		return eDto;
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post ", "post id", postId));

		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		Post updatedPost = this.postRepo.save(post);
		esService.updateEsPost(postDto, postId);
		postDto.setAddedDate(new Date().toString());
		postDto.setImageName("default.png");
		PostDto eDto = esService.updateEsPost(postDto, postId);
		// return this.modelMapper.map(updatedPost, PostDto.class);
		return eDto;
	}

	@Override
	public void deletePost(Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post ", "post id", postId));
		esService.deleteEsPost(postId);
		this.postRepo.delete(post);

	}

	@Override
	public List<PostDto> getAllPost1() {

		/*
		 * List<Post> categories = this.postRepo.findAll();
		 * 
		 * List<PostDto> catDtos = categories.stream().map((cat) ->
		 * this.modelMapper.map(cat, PostDto.class)) .collect(Collectors.toList());
		 */
		List<PostDto> eList = esService.getEsAllPost1();
		// return catDtos;
		return eList;
	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pagePost = this.postRepo.findAll(p);

		List<Post> allPosts = pagePost.getContent();

		List<PostDto> postDtos = allPosts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();

		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());

		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
		PostDto eList = esService.getEsPostById(postId);
		System.out.println("#########################" + eList);
		return this.modelMapper.map(post, PostDto.class);
	}

	// here based on child id i have fetched parent all data
	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {

		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
		List<Post> posts = this.postRepo.findByCategory(cat);

		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		// List<PostDto> eList = esService.getEsPostsByCategory(categoryId);

		return postDtos;
	}

//here based on child id i have fetched parent all data
	@Override
	public List<PostDto> getPostsByUser(Integer userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", "userId ", userId));

		List<Post> posts = this.postRepo.findByUser(user);

		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return postDtos;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts = this.postRepo.searchByTitle("%" + keyword + "%");
		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		List<PostDto> e=esService.searchEsPosts(keyword);
		return postDtos;
	}

}
