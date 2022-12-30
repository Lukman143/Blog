package com.sk.elasticsearch.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(indexName = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostElasticsearch {

	@Id
	@Field(type = FieldType.Keyword)
	private String postId;

	@Field(type = FieldType.Text)
	private String title;

	@Field(type = FieldType.Text)
	private String content;

	@Field(type = FieldType.Text)
	private String imageName;

	@Field(type = FieldType.Text)
	private String addedDate;

	@Field(type = FieldType.Nested, includeInParent = true)
	private CategoryElasticsearch category;

	@Field(type = FieldType.Nested, includeInParent = true)
	private UserElasticsearch user;

	@Field(type = FieldType.Nested, includeInParent = true)
	private Set<CommentElasticsearch> comments = new HashSet<>();

	

}
