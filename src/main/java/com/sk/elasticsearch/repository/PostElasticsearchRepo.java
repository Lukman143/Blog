package com.sk.elasticsearch.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sk.elasticsearch.entity.PostElasticsearch;
import com.sk.entity.Category;
import com.sk.entity.Post;
import com.sk.entity.User;

@Repository
public interface PostElasticsearchRepo extends ElasticsearchRepository<PostElasticsearch, String> {

	/*
	 * List<Post> findByUser(User user);
	 * 
	 * List<Post> findByCategory(Category category);
	 * 
	 * @Query("select p from Post p where p.title like :key") List<Post>
	 * searchByTitle(@Param("key") String title);
	 */

}