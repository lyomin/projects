package com.lyomin.first.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lyomin.first.domain.Post;
import com.lyomin.first.post.model.PostListItem;

public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query(value = "SELECT new com.lyomin.first.post.model.PostListItem(p.id, p.title, p.shortContent, p.createdAt) FROM Post p WHERE p.visible = true",
			countQuery = "SELECT COUNT(p.id) FROM Post p WHERE p.visible = true")
	Page<PostListItem> findPublished(Pageable pageable);

}
