package com.sk.elasticsearch.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Document(indexName = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentElasticsearch {

	@Id
	@Field(type = FieldType.Keyword)
	private String id;
	
	@Field(type = FieldType.Text)
	private String content;

	/*
	 * @ManyToOne private PostElasticsearch post;
	 */
	//@Field(type = FieldType.Nested, includeInParent = true)
	//private PostElasticsearch post;
}
