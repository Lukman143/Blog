package com.sk.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.sk.elasticsearch.entity.CommentElasticsearch;

@Repository
public interface CommentElasticsearchRepo extends ElasticsearchRepository<CommentElasticsearch, String> {

}
