package com.sk.elasticsearch.preservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.elasticsearch.entity.CategoryElasticsearch;
import com.sk.elasticsearch.entity.CommentElasticsearch;
import com.sk.elasticsearch.entity.PostElasticsearch;
import com.sk.elasticsearch.entity.UserElasticsearch;
import com.sk.elasticsearch.repository.CategoryElasticsearchRepo;
import com.sk.elasticsearch.repository.CommentElasticsearchRepo;
import com.sk.elasticsearch.repository.PostElasticsearchRepo;
import com.sk.elasticsearch.repository.UserElasticsearchRepo;
import com.sk.elasticsearch.service.IndexService;
import com.sk.elasticserach.helper.Indices;
import com.sk.exceptions.ResourceNotFoundException;
import com.sk.payloads.CategoryDto;
import com.sk.payloads.CommentDto;
import com.sk.payloads.PostDto;
import com.sk.payloads.UserDto;

@Service
public class PostElasticsearchService {

	@Autowired
	private PostElasticsearchRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserElasticsearchRepo userRepo;

	@Autowired
	private CategoryElasticsearchRepo categoryRepo;

	@Autowired
	private CommentElasticsearchRepo cRepo;

	@Autowired
	private IndexService service;

	// ObjectMapper is use to convert map data into User List or any type of list
	@Autowired
	private ObjectMapper objectMapper;

	RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

	public PostDto createEsPost(PostDto postDto, Integer userId, Integer categoryId) {
		boolean b = service.isIndexAvailableOrNot(Indices.POST_INDEX);

		UserElasticsearch user = this.userRepo.findById(userId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

		CategoryElasticsearch category = this.categoryRepo.findById(categoryId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id ", categoryId));

		PostElasticsearch post = this.modelMapper.map(postDto, PostElasticsearch.class);
		post.setImageName("default.png");

		post.setUser(user);
		post.setCategory(category);

		PostElasticsearch newPost = this.postRepo.save(post);

		PostDto dto = new PostDto();
		dto.setPostId(Integer.parseInt(newPost.getPostId()));
		dto.setTitle(newPost.getTitle());
		dto.setContent(newPost.getContent());
		dto.setImageName(newPost.getImageName());
		dto.setAddedDate(postDto.getAddedDate());

		UserElasticsearch uEs = newPost.getUser();
		CategoryElasticsearch catEs = newPost.getCategory();
		Set<CommentElasticsearch> cEs = newPost.getComments();
		Set<CommentDto> cDto = cEs.stream().map((com) -> this.modelMapper.map(com, CommentDto.class))
				.collect(Collectors.toSet());

		UserDto uDto = this.modelMapper.map(uEs, UserDto.class);
		CategoryDto catDto = this.modelMapper.map(catEs, CategoryDto.class);

		dto.setCategory(catDto);
		dto.setUser(uDto);
		dto.setComments(cDto);

		return dto;

	}

	public PostDto updateEsPost(PostDto postDto, Integer postId) {

		PostElasticsearch post = this.postRepo.findById(postId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("Post ", "post id", postId));

		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());

		PostElasticsearch updatedPost = this.postRepo.save(post);

		PostDto dto = new PostDto();
		dto.setPostId(Integer.parseInt(updatedPost.getPostId()));
		dto.setTitle(updatedPost.getTitle());
		dto.setContent(updatedPost.getContent());
		dto.setImageName(updatedPost.getImageName());
		dto.setAddedDate(postDto.getAddedDate());

		UserElasticsearch uEs = updatedPost.getUser();
		CategoryElasticsearch catEs = updatedPost.getCategory();
		Set<CommentElasticsearch> cEs = updatedPost.getComments();
		Set<CommentDto> cDto = cEs.stream().map((com) -> this.modelMapper.map(com, CommentDto.class))
				.collect(Collectors.toSet());

		UserDto uDto = this.modelMapper.map(uEs, UserDto.class);
		CategoryDto catDto = this.modelMapper.map(catEs, CategoryDto.class);

		dto.setCategory(catDto);
		dto.setUser(uDto);
		dto.setComments(cDto);

		return dto;
	}

	public void deleteEsPost(Integer postId) {

		PostElasticsearch post = this.postRepo.findById(postId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("Post ", "post id", postId));
		this.postRepo.delete(post);
	}

	public List<PostDto> getEsAllPost1() {

		List<CommentElasticsearch> cEs = new ArrayList<>();
		Iterable<CommentElasticsearch> citr = this.cRepo.findAll();

		for (CommentElasticsearch lc : citr) {
			cEs.add(lc);
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + lc);

		}

		List<PostElasticsearch> list = new ArrayList<PostElasticsearch>();
		List<PostDto> pList = new ArrayList<PostDto>();

		Iterable<PostElasticsearch> itr = this.postRepo.findAll();

		for (PostElasticsearch l : itr) {
			list.add(l);
		}
		CommentDto cDto1 = null;
		int i = 0;

		List<CommentElasticsearch> cEsss = new ArrayList<>();

		for (PostElasticsearch li : list) {
			PostDto dto = new PostDto();
			dto.setPostId(Integer.parseInt(li.getPostId()));

			dto.setTitle(li.getTitle());
			dto.setContent(li.getContent());
			dto.setImageName(li.getImageName());
			dto.setAddedDate(li.getAddedDate());

			UserElasticsearch uEs = li.getUser();
			CategoryElasticsearch catEs = li.getCategory();
			CommentElasticsearch ccc = this.cRepo.findById(li.getPostId()).orElseThrow(
					() -> new ResourceNotFoundException("Comment ", "comment id", Integer.parseInt(li.getPostId())));
			cEsss.add(ccc);
			Set<CommentDto> cDto = cEsss.stream().map((com) -> this.modelMapper.map(com, CommentDto.class))
					.collect(Collectors.toSet());
			UserDto uDto = this.modelMapper.map(uEs, UserDto.class);
			CategoryDto catDto = this.modelMapper.map(catEs, CategoryDto.class);

			dto.setCategory(catDto);
			dto.setUser(uDto);

			dto.setComments(cDto);

			pList.add(dto);
			cEsss = new ArrayList<>();

		}
		return pList;
	}

	/*
	 * public PostResponse getEsAllPost(Integer pageNumber, Integer pageSize, String
	 * sortBy, String sortDir) {
	 * 
	 * 
	 * PageRequest firstPageWithTwoElements = PageRequest.of(0, 2);
	 * 
	 * PageRequest secondPageWithFiveElements = PageRequest.of(1, 5);
	 * Page<PostElasticsearch> allProducts =
	 * postRepo.findAll(firstPageWithTwoElements);
	 * 
	 * List<PostElasticsearch> allTenDollarProducts = postRepo.findAllByPostId(10,
	 * secondPageWithFiveElements); //postRepo.findAllByPrice(10,
	 * secondPageWithFiveElements);
	 * System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+allProducts);
	 * 
	 * System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+allTenDollarProducts);
	 * 
	 * 
	 * Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() :
	 * Sort.by(sortBy).descending();
	 * 
	 * Pageable p = PageRequest.of(pageNumber, pageSize, sort);
	 * 
	 * Page<PostElasticsearch> pagePost = this.postRepo.findAll(p);
	 * 
	 * List<PostElasticsearch> allPosts = pagePost.getContent();
	 * 
	 * List<PostDto> postDtos = allPosts.stream().map((post) ->
	 * this.modelMapper.map(post, PostDto.class)) .collect(Collectors.toList());
	 * 
	 * PostResponse postResponse = new PostResponse();
	 * 
	 * postResponse.setContent(postDtos);
	 * postResponse.setPageNumber(pagePost.getNumber());
	 * postResponse.setPageSize(pagePost.getSize());
	 * postResponse.setTotalElements(pagePost.getTotalElements());
	 * 
	 * postResponse.setTotalPages(pagePost.getTotalPages());
	 * postResponse.setLastPage(pagePost.isLast());
	 * 
	 * return null; // postResponse; }
	 */

	public PostDto getEsPostById(Integer postId) {
		PostElasticsearch post = this.postRepo.findById(postId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));

		PostDto dto = new PostDto();
		dto.setPostId(Integer.parseInt(post.getPostId()));
		dto.setTitle(post.getTitle());
		dto.setContent(post.getContent());
		dto.setImageName(post.getImageName());
		dto.setAddedDate(post.getAddedDate());
		UserElasticsearch uEs = post.getUser();
		CategoryElasticsearch catEs = post.getCategory();
		Set<CommentElasticsearch> cEs = post.getComments();
		Set<CommentDto> cDto = cEs.stream().map((com) -> this.modelMapper.map(com, CommentDto.class))
				.collect(Collectors.toSet());
		UserDto uDto = this.modelMapper.map(uEs, UserDto.class);
		CategoryDto catDto = this.modelMapper.map(catEs, CategoryDto.class);
		dto.setCategory(catDto);
		dto.setUser(uDto);
		dto.setComments(cDto);
		return dto;
	}

	/*
	 * // here based on child id i have fetched parent all data
	 * 
	 * public List<PostDto> getEsPostsByCategory(Integer categoryId) {
	 * 
	 * CategoryElasticsearch cat = this.categoryRepo.findById(categoryId.toString())
	 * .orElseThrow(() -> new ResourceNotFoundException("Category", "category id",
	 * categoryId)); List<PostElasticsearch> posts =
	 * this.postRepo.findByCategoryElasticsearch(cat);
	 * 
	 * 
	 * List<PostDto> postDtos = posts.stream().map((post) ->
	 * this.modelMapper.map(post, PostDto.class)) .collect(Collectors.toList());
	 * 
	 * 
	 * return null; }
	 */
//here based on child id i have fetched parent all data

	/*
	 * public List<PostDto> getEsPostsByUser(Integer userId) {
	 * 
	 * UserElasticsearch user = this.userRepo.findById(userId.toString())
	 * .orElseThrow(() -> new ResourceNotFoundException("User ", "userId ",
	 * userId));
	 * 
	 * List<PostElasticsearch> posts = this.postRepo.findByUserElasticsearch(user);
	 * 
	 * List<PostDto> postDtos = posts.stream().map((post) ->
	 * this.modelMapper.map(post, PostDto.class)) .collect(Collectors.toList());
	 * 
	 * return postDtos; }
	 */

	public List<PostDto> searchEsPosts(String keyword) {
		/*
		 * List<PostElasticsearch> posts = this.postRepo.searchByTitle("%" + keyword +
		 * "%"); List<PostDto> postDtos = posts.stream().map((post) ->
		 * this.modelMapper.map(post, PostDto.class)) .collect(Collectors.toList());
		 * return postDtos;
		 */
		List<PostDto> userList = new ArrayList<>();

		SearchRequest searchRequest = new SearchRequest(Indices.POST_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		try {
			QueryBuilder query = QueryBuilders.boolQuery()
					.should(new WildcardQueryBuilder("title", "*" + keyword + "*"));
			searchSourceBuilder.query(query);
			searchRequest.source(searchSourceBuilder);
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

			if (searchResponse.getHits().getTotalHits().value > 0) {

				SearchHit[] searchHit = searchResponse.getHits().getHits();
				for (SearchHit hit : searchHit) {
					Map<String, Object> map = hit.getSourceAsMap();

					
					String pId = map.get("postId").toString();
					System.out.println(pId);
					CommentElasticsearch cPost = this.cRepo.findById(pId)
							.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", 0));

					map.put("comments", cPost);
					String comments =map.get("comments").toString();
					System.out.println("#############################################"+comments);
					this.modelMapper.map(comments, CommentDto.class);
					
					
					userList.add(this.modelMapper.map(map, PostDto.class));
					System.out.println("#############################################"+userList);

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return userList;

	}

}
