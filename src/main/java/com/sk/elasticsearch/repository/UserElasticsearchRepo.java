package com.sk.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.sk.elasticsearch.entity.UserElasticsearch;

@Repository
public interface UserElasticsearchRepo extends ElasticsearchRepository<UserElasticsearch, String> {

}
