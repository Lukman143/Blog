package com.sk.elasticsearch.entity;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserElasticsearch {
	@Id
	@Field(type = FieldType.Keyword)
	private String id;

	@Field(type = FieldType.Text)
	private String name;

	@Field(type = FieldType.Text)
	private String email;

	@Field(type = FieldType.Text)
	private String password;

	@Field(type = FieldType.Text)
	private String about;

	//@Field(type = FieldType.Nested, includeInParent = true)
	//private List<PostElasticsearch> post;

}
