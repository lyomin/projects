package com.test.helloworld;

import java.net.URI;
import java.time.Duration;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import reactor.core.publisher.Mono;

@Component
public class ApplicationEcents {
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		System.out.println("application is ready");
		try {
	        WebSocketClient client = new org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient();
	        client.execute(
	          URI.create("ws://localhost:8082/event-emitter"), 
	          session -> session.send(
	            Mono.just(session.textMessage("event-spring-reactive-client-websocket"))
	           ).thenMany(
	        		session.receive()
	        		   .map(WebSocketMessage::getPayloadAsText)
	        		   .log()
	            ).then())
	        	.doFinally(t -> System.out.println("Finaly " + t.toString()))
	            .block(Duration.ofSeconds(10L));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("END");
		}
		
	}
}
