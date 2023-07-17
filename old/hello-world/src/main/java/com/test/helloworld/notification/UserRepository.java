package com.test.helloworld.notification;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.test.helloworld.notification.domain.Users;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<Users, Long> {

	Mono<Users> findByName(String userName);

	@Query("SELECT u.* FROM users u")
	Flux<Users> findAll(Pageable pageable);

}
