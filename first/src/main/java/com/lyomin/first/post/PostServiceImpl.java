package com.lyomin.first.post;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lyomin.first.domain.Post;
import com.lyomin.first.domain.Section;
import com.lyomin.first.post.model.PostListItem;
import com.lyomin.first.repository.CommentsRepository;
import com.lyomin.first.repository.PostRepository;
import com.lyomin.first.repository.SectionRepository;

@Component
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;
	private SectionRepository sectionRepository;
	private CommentsRepository commentsRepository;
	
	@Autowired
	public PostServiceImpl(PostRepository postRepository, SectionRepository sectionRepository) {
		super();
		this.postRepository = postRepository;
		this.sectionRepository = sectionRepository;
	}
	
	@Override
	public Optional<Post> getById(Integer id) {
		return postRepository.findById(id).map(p -> {
			p.setParts(
					sectionRepository.findByPostId(id)
			);
			return p;
		});
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		commentsRepository.deleteByPostId(id);
		sectionRepository.deleteByPostId(id);
		postRepository.deleteById(id);
	}

	@Override
	public Post save(Post post) {
		
		if (post.getId() == null)
			post.setCreatedAt(Instant.now());
		
		List<Section> sections = post.getParts();
		post = postRepository.save(post);
		int i = 1;
		for (Section sec : sections) {
			sec.setOrder(i++);
			sec.setPost(post);
		};
		sectionRepository.saveAll(sections);
		
		return post;
	}

	@Override
	public Page<Post> list(Pageable pageable) {
		return postRepository.findAll(pageable);
	}

	@Override
	public Page<PostListItem> listPage(Pageable pageable) {
		return postRepository.findPublished(pageable);
	}

}
