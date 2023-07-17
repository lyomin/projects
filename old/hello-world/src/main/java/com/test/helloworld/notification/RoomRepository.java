package com.test.helloworld.notification;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.test.helloworld.notification.domain.Message;
import com.test.helloworld.notification.domain.Rooms;

import reactor.core.publisher.Flux;

public interface RoomRepository extends R2dbcRepository<Rooms, Integer> {
	
	@Query("SELECT r.* FROM rooms r")
	public Flux<Rooms> findAll(Pageable page);
}
