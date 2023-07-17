package com.test.helloworld.controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.helloworld.controller.model.NewRequest;
import com.test.helloworld.controller.model.UserDeleteRequest;
import com.test.helloworld.notification.UserRepository;
import com.test.helloworld.notification.domain.Rooms;
import com.test.helloworld.notification.domain.Users;

import reactor.core.publisher.Mono;

@RestController
public class UserRestController {
	
	UserRepository userRepository;
	
	@Autowired
	public UserRestController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}



	@PostMapping("api/user/create")
	public Mono<Users> createUser(
			@Validated @RequestBody NewRequest body
	) {
		Instant now = Instant.now();
		return userRepository.save(new Users(null, body.getName(),now,now));
	}
	
	@PostMapping("api/user/delete")
	public Mono<Void> deleteUser(
			@Validated @RequestBody UserDeleteRequest body
	) {
		return userRepository.deleteById(body.getId());
	}
	
	@GetMapping(value = "api/user/all")
	public Mono<PageImpl<Users>> getUserRooms(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
			) {
		PageRequest request = PageRequest.of(page, size);
		return userRepository
				.findAll(request)
				.collectList()
				.zipWith(userRepository.count())
				.mapNotNull((tuple) -> {
					return new PageImpl<Users>(
							tuple.getT1(),
							request,
							tuple.getT2()
					);
				});
	}
}
