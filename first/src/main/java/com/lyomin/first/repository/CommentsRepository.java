package com.lyomin.first.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.lyomin.first.domain.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {
	@Transactional
	@Modifying
	public void deleteByPostId(Integer postId);
}
