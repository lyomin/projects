package com.test.helloworld.controller;

import java.time.Instant;

import javax.print.attribute.standard.Media;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.helloworld.controller.model.DeleteRoomRequest;
import com.test.helloworld.controller.model.NewMessageRequest;
import com.test.helloworld.controller.model.NewRequest;
import com.test.helloworld.notification.MessagesService;
import com.test.helloworld.notification.RoomRepository;
import com.test.helloworld.notification.UserRoomsCrudServcie;
import com.test.helloworld.notification.domain.Message;
import com.test.helloworld.notification.domain.Rooms;
import com.test.helloworld.notification.domain.UserRooms;
import com.test.helloworld.notification.domain.Users;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MessageRoomRestController {
	
	UserRoomsCrudServcie userRoomService;
	RoomRepository roomsRepository;
	
	public MessageRoomRestController(UserRoomsCrudServcie userRoomService,
			RoomRepository roomsRepository) {
		this.userRoomService = userRoomService;
		this.roomsRepository = roomsRepository;
	}
	
	@PostMapping("api/room/create")
	public Mono<Rooms> createRoom(
			@Validated @RequestBody NewRequest body
	) {
		return userRoomService.createRoom(body.getName());
	}
	
	@PostMapping(value = "api/room/user/add")
	public Mono<UserRooms> addMember(
			@RequestBody UserRooms body
			) {
		return userRoomService.insert(body)    
	    .then(
	    		userRoomService.findById(body.getRoomId(),body.getUserId())
	    );
	}
	
	@PostMapping(value = "api/room/user/remove", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<Void> deleteMember(
			@RequestBody UserRooms body	
	) {
		return userRoomService.deleteById(body.getRoomId(), body.getUserId())
				.then();
	}
	
	@PostMapping(value = "api/room/delete")
	public Mono<Void> deleteRoom(
			@RequestBody DeleteRoomRequest body
			) {
		return userRoomService.deleteByRoomId(body.getId())
				.then();
	}
	
	@GetMapping(value = "api/room/user/all")
	public Mono<PageImpl<Rooms>> getUserRooms(
			@RequestParam(value = "user-id") long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
			) {
		PageRequest request = PageRequest.of(page, size);
		return userRoomService
				.findRoomByUserId(userId, request)
				.collectList()
				.zipWith(userRoomService.countByUserId(userId))
				.mapNotNull((tuple) -> {
					return new PageImpl<Rooms>(
							tuple.getT1(),
							request,
							tuple.getT2()
					);
				});
	}
	
	@GetMapping(value = "api/room/member/all")
	public Mono<PageImpl<Users>> getRoomUsers(
			@RequestParam(value = "room-id") int roomId,
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
			) {
		PageRequest request = PageRequest.of(page, size);
		return userRoomService
				.findMembersByRoomId(roomId, request)
				.collectList()
				.zipWith(userRoomService.countByRoomId(roomId))
				.mapNotNull((tuple) -> {
					return new PageImpl<Users>(
							tuple.getT1(),
							request,
							tuple.getT2()
					);
				});
	}
	
	@GetMapping(value = "api/room/all")
	public Mono<PageImpl<Rooms>> getUserRooms(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
			) {
		PageRequest request = PageRequest.of(page, size);
		return roomsRepository
				.findAll(request)
				.collectList()
				.zipWith(roomsRepository.count())
				.mapNotNull((tuple) -> {
					return new PageImpl<Rooms>(
							tuple.getT1(),
							request,
							tuple.getT2()
					);
				});
	}
	
	@GetMapping(value = "api/room/flux")
	public Flux<Rooms> getUserRooms(
			) {
		return roomsRepository
				.findAll();
	}
}
