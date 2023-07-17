package com.test.helloworld.notification;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.helloworld.notification.domain.Message;
import com.test.helloworld.notification.domain.Rooms;
import com.test.helloworld.notification.domain.Users;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MessagesService {
	
	MessageRepository msgRepository;
	UserRepository memberRepository;
	
	public final static Flux<Instant> intervalEmiter = getIntervalEmiter();
	
	private static Flux<Instant> getIntervalEmiter() {
			final int interevalSec = 5;
			return Flux.interval(Duration.ofSeconds(interevalSec))
				.map(m -> Instant.now().minusSeconds(interevalSec));
	}
	
	@Autowired
	public MessagesService(MessageRepository msgRepository, UserRepository memberRepository) {
		super();
		this.msgRepository = msgRepository;
		this.memberRepository = memberRepository;
	}


	@Transactional
	public Mono<Message> createMessage(String msgText, String userName, Integer roomId) {
				
		Instant now = Instant.now();
		
		Mono<Users> userOpt = memberRepository.findByName(userName);
		
		return userOpt.defaultIfEmpty(new Users(null, userName, null, null))
		.map(user -> {
			user.setLastMsgAt(now);
			user.setRespondedAt(now);
			return user;
		})
		.flatMap(memberRepository::save)
		.then(Mono.just(new Message(null, roomId, msgText, now, now)))
		.flatMap(msgRepository::save);

	}
	@Transactional
	public Mono<Message> updateMessage(Long id, String msgText) {
		Mono<Message> msgMono = msgRepository.findById(id);
		return msgMono.map(m -> {
			m.setMessage(msgText);
			m.setUpdatedAt(Instant.now());
			return m;
		}).flatMap(msgRepository::save);
	}
	
	public void viewMessage(String msgText, String userName) {
		Instant now = Instant.now();
		
		Mono<Users> userOpt = memberRepository.findByName(userName);
		userOpt.map(user -> {user.setRespondedAt(now); return user;})
		.flatMap(memberRepository::save);

	}
	
	public Mono<Page<Message>> getLastMsg(Integer roomId) {
		PageRequest request = PageRequest.of(
				0, 10, Sort.by("createdAt").descending()
		);
		return msgRepository
			.findByRoomId(
				roomId,
				request
			).collectList()
			.zipWith(msgRepository.countByRoomId(roomId))
			.map(t -> new PageImpl<>(t.getT1(), request, t.getT2()));
	}
	
	public Mono<Page<Message>> getMsg(Integer roomId, Pageable pageable) {
		PageRequest request = PageRequest.of(
				pageable.getPageNumber(),
				pageable.getPageSize(),
				Sort.by("createdAt").descending()
		);
		return msgRepository
			.findByRoomId(
				roomId,
				request
			).collectList()
			.zipWith(msgRepository.countByRoomId(roomId))
			.map(t -> new PageImpl<>(t.getT1(), request, t.getT2()));
		
		
	}
	
	public Flux<Message> getUpdates(Integer roomId, Instant from) {
		return msgRepository
			.streamUpdates(from, roomId);
	}
	
	public Flux<Message> streamUpdates(Integer roomId) {
		return intervalEmiter
			.flatMap(tmsp -> msgRepository
					.streamUpdates(tmsp, roomId));
	}
	
	public Flux<Message> getForUserUpdates(Long userId, Instant from) {
		return msgRepository
			.streamUserRoomUpdates(from, userId);
	}
	
	public Flux<Message> streamUserUpdates(Long userId) {
		return intervalEmiter
			.flatMap(tmsp -> msgRepository
					.streamUserRoomUpdates(tmsp, userId))/*
			.doFinally((s) -> System.out.println("doFinally"))*/;
	}
	
	/*@Query(value = "SELECT m FROM Message m WHERE m.id > :id")
	Page<Message> findFormId(@Param("id") Long id, Pageable pageable);
	
	@Query(value = "SELECT m FROM Message m WHERE m.id < :id")
	Page<Message> findToId(@Param("id") Long id, Pageable pageable);*/
	
	public Mono<Page<Message>> getNextMsg(Long id, Integer roomId) {

		
		PageRequest request = PageRequest.of(
				0,
				10,
				Sort.by("createdAt").descending()
		);
		return msgRepository
			.findFormId(
				id, roomId, request
			).collectList()
			.zipWith(msgRepository.findFormIdCount(id, roomId))
			.map(t -> new PageImpl<>(t.getT1(), request, t.getT2()));
	}
	
	public Mono<Page<Message>> getPrievMsg(Long id, Integer roomId) {
		
		PageRequest request = PageRequest.of(
				0,
				10,
				Sort.by("createdAt").descending()
		);
		return msgRepository
			.findToId(
				id, roomId, request
			).collectList()
			.zipWith(msgRepository.findToIdCount(id, roomId))
			.map(t -> new PageImpl<>(t.getT1(), request, t.getT2()));
	}
}
