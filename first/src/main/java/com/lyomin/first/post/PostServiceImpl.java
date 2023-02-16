package com.lyomin.first.post;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.lyomin.first.domain.Post;
import com.lyomin.first.repository.PostRepository;

@Component
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;
	
	@Autowired
	public PostServiceImpl(PostRepository postRepository) {
		super();
		this.postRepository = postRepository;
	}
	
	@Override
	public Optional<Post> getById(Integer id) {
		return postRepository.findById(id);
	}

	@Override
	public void deleteById(Integer id) {
		postRepository.deleteById(id);
	}

	@Override
	public Post save(Post post) {
		
		if (post.getId() == null)
			post.setCreatedAt(Instant.now());
		return postRepository.save(post);
	}

	@Override
	public Page<Post> list(Pageable pageable) {
		return postRepository.findAll(pageable);
	}

}
