package com.sk.elasticsearch.preservice;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sk.elasticsearch.entity.CommentElasticsearch;
import com.sk.elasticsearch.entity.PostElasticsearch;
import com.sk.elasticsearch.repository.CommentElasticsearchRepo;
import com.sk.elasticsearch.repository.PostElasticsearchRepo;
import com.sk.elasticsearch.service.IndexService;
import com.sk.elasticserach.helper.Indices;
import com.sk.entity.Comment;
import com.sk.exceptions.ResourceNotFoundException;
import com.sk.payloads.CommentDto;

@Service
public class CommentElasticsearchService {
	@Autowired
	private CommentElasticsearchRepo commentRepo;

	@Autowired
	private PostElasticsearchRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IndexService service;

	public CommentDto createEComment(CommentDto commentDto, Integer postId) {
		boolean b = service.isIndexAvailableOrNot(Indices.COMMENT_INDEX);

		PostElasticsearch post = this.postRepo.findById(postId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id ", postId));

		CommentElasticsearch comment = this.modelMapper.map(commentDto, CommentElasticsearch.class);

		comment.setPost(post);

		CommentElasticsearch savedComment = this.commentRepo.save(comment);

		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	public void deleteEComment(Integer commentId) {

		CommentElasticsearch com = this.commentRepo.findById(commentId.toString())
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "CommentId", commentId));
		this.commentRepo.delete(com);
	}

}
