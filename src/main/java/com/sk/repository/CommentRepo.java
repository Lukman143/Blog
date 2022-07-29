package com.sk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.entity.Comment;

public interface CommentRepo  extends JpaRepository<Comment	, Integer> {

}
