package com.sk.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sk.elasticsearch.preservice.CommentElasticsearchService;
import com.sk.entity.Comment;
import com.sk.entity.Post;
import com.sk.exceptions.ResourceNotFoundException;
import com.sk.payloads.CommentDto;
import com.sk.repository.CommentRepo;
import com.sk.repository.PostRepo;
import com.sk.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CommentElasticsearchService eService;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id ", postId));

		Comment comment = this.modelMapper.map(commentDto, Comment.class);

		comment.setPost(post);

		Comment savedComment = this.commentRepo.save(comment);

		CommentDto eComment = eService.createEComment(commentDto, postId);
		// return eComment;

		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {

		Comment com = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "CommentId", commentId));
		eService.deleteEComment(commentId);

		this.commentRepo.delete(com);
	}

}
