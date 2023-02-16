package com.lyomin.first.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lyomin.first.domain.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
