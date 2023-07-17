package com.test.helloworld.notification;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.test.helloworld.notification.domain.Message;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageRepository extends R2dbcRepository <Message, Long> {
	
	public Flux<Message> findByRoomId(Integer roomId, Pageable page);
	
	public Mono<Long> countByRoomId(Integer roomId);
	
	@Query(value = "SELECT m.* FROM Messages m WHERE m.id > :id AND m.room_id = :roomId")
	Flux<Message> findFormId(@Param("id") Long id, @Param("roomId") Integer roomId, Pageable pageable);
	
	@Query(value = "SELECT COUNT(m.id) FROM Messages m WHERE m.id > :id AND m.room_id = :roomId")
	Mono<Long> findFormIdCount(@Param("id") Long id, @Param("roomId") Integer roomId);
	
	@Query(value = "SELECT m.* FROM Messages m WHERE m.id < :id AND m.room_id = :roomId")
	Flux<Message> findToId(@Param("id") Long id, @Param("roomId") Integer roomId, Pageable pageable);
	
	@Query(value = "SELECT COUNT(m.id) FROM Messages m WHERE m.id < :id AND m.room_id = :roomId")
	Mono<Long> findToIdCount(@Param("id") Long id, @Param("roomId") Integer roomId);
	
	@Query(value = "SELECT m.* FROM Messages m WHERE m.updated_At > :limit AND m.room_id = :roomId")
	Flux<Message> findUpdates(@Param("limit") Instant windowLimit, @Param("roomId") Integer roomId, Pageable pageable);
	
	@Query(value = "SELECT m.* FROM Messagse m WHERE m.updated_At > :limit AND m.room_id = :roomId")
	Mono<Long> findUpdatesCount(@Param("limit") Instant windowLimit, @Param("roomId") Integer roomId);
	
	@Query(value = "SELECT m.* FROM Messages m WHERE m.updated_At >= :limit AND m.room_id = :roomId")
	Flux<Message> streamUpdates(@Param("limit") Instant windowLimit, @Param("roomId") Integer roomId);

	@Query(value = "SELECT m.* FROM Messages m JOIN user_rooms ur ON m.room_id = ur.room_id WHERE m.updated_At >= :limit AND ur.user_id = :userId")
	public Flux<Message> streamUserRoomUpdates(@Param("limit") Instant from, @Param("userId") Long userId);
}
