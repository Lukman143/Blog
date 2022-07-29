package com.sk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
