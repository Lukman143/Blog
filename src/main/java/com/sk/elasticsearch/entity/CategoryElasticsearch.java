package com.sk.elasticsearch.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(indexName = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryElasticsearch {

	@Id
	@Field(type = FieldType.Keyword)
	private String categoryId;

	@Field(type = FieldType.Text)
	private String categoryTitle;

	@Field(type = FieldType.Text)
	private String categoryDescription;

	@Field(type = FieldType.Nested, includeInParent = true)
    private List<PostElasticsearch> posts = new ArrayList<>();

}