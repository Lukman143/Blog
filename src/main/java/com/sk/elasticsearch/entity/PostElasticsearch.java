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
@Document(indexName = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "category_id") private CategoryElasticsearch category;
	 */
	@Field(type = FieldType.Nested, includeInParent = true)
	private List<CategoryElasticsearch> category;

	/*
	 * @ManyToOne private UserElasticsearch user;
	 * 
	 * @OneToMany(mappedBy = "post",fetch = FetchType.EAGER,cascade =
	 * CascadeType.ALL) private Set<CommentElasticsearch> comments=new HashSet<>();
	 */
	//@Field(type = FieldType.Nested, includeInParent = true)
	//private Set<CommentElasticsearch> comments = new HashSet<>();

}
