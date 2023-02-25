package com.lyomin.first.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lyomin.first.domain.Post;
import com.lyomin.first.post.PostService;
import com.lyomin.first.post.model.PostListItem;

@RestController
@RequestMapping("posts")
public class PostConteoller {
	
	PostService postServie;
	
	@Autowired
	public PostConteoller(PostService postServie) {
		super();
		this.postServie = postServie;
	}
	
	@GetMapping(path = "/all")
	public Page<PostListItem> getAll(Pageable pageable) {
		
		if (pageable.getSort().isUnsorted())
			pageable = PageRequest.of(
					pageable.getPageNumber(), pageable.getPageSize(), 
					Sort.by(Direction.DESC, "createdAt")
			);
		
		return postServie.listPage(pageable);
	}

	@PostMapping(path = "/")
	public ResponseEntity<Post> create(@RequestBody Post body) {
		return ResponseEntity.ok(
				postServie.save(body)
		);
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Post> get(@PathVariable("id") Integer id) {
		return ResponseEntity.of(postServie.getById(id));
	}
	
	@DeleteMapping(path = "/{id}")
	public void delete(@PathVariable("id") Integer id) {
		postServie.deleteById(id);
	}
}
