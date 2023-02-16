package com.lyomin.first.post;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lyomin.first.domain.Post;

@Service
public interface PostService {
	public Optional<Post> getById(Integer id);
	public void deleteById(Integer id);
	public Post save(Post post);
	public Page<Post> list(Pageable pageable);
}
