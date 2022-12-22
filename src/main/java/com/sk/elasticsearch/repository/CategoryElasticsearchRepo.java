package com.sk.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.sk.elasticsearch.entity.CategoryElasticsearch;

@Repository
public interface CategoryElasticsearchRepo extends ElasticsearchRepository<CategoryElasticsearch, String> {

}
