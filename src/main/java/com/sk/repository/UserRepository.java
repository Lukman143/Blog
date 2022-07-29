package com.sk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
