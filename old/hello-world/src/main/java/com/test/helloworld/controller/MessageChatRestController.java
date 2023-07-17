package com.test.helloworld.controller;

import java.time.Instant;

import javax.print.attribute.standard.Media;

import org.springframework.data.domain.Page;
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

import com.test.helloworld.controller.model.NewMessageRequest;
import com.test.helloworld.notification.MessagesService;
import com.test.helloworld.notification.domain.Message;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MessageChatRestController {
	
	MessagesService messageService;
	
	public MessageChatRestController(MessagesService messageService) {
		this.messageService = messageService;
	}
	
	@PostMapping("api/message/room/post")
	public Mono<Message> createMessage(
			@Validated @RequestBody NewMessageRequest body
	) {
		return messageService.createMessage(body.getMessage(), body.getUserName(), body.getRoom());
	}
	
	@GetMapping(value = "api/message/room/updates")
	public Flux<Message> getMessageUpdate(
			@RequestParam(value = "from-ms", defaultValue = "-1") long limit,
			@RequestParam(value = "room-id") int roomId
			) {
		return messageService.getUpdates(
			roomId,
			limit < 0 ? Instant.now() : Instant.ofEpochMilli(limit)
		);
	}
	
	@GetMapping(value = "api/message/room/updates-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Message> getMessageUpdate(
			@RequestParam(value = "room-id") int roomId		
	) {
		return messageService.streamUpdates(roomId);
	}
	
	@GetMapping(value = "api/message/user/updates")
	public Flux<Message> getMessageAllUpdate(
			@RequestParam(value = "from-ms", defaultValue = "-1") long limit,
			@RequestParam(value = "user-id") long userId
			) {
		return messageService.getForUserUpdates(
			userId,
			limit < 0 ? Instant.now() : Instant.ofEpochMilli(limit)
		);
	}
	
	@GetMapping(value = "api/message/user/updates-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Message> getAllRoomMessageUpdate(
			@RequestParam(value = "user-id") long userID		
	) {
		return messageService.streamUserUpdates(userID);
	}
	
	@GetMapping(value = "api/message/room/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public @ResponseBody Mono<Page<Message>> getMessages(
			@RequestParam(value = "room-id") int roomId,
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
		return messageService.getMsg(roomId, PageRequest.of(page, size));
	}
}
