package com.test.helloworld.websocket;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Flow.Publisher;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component("ReactiveWebSocketHandler")
public class ReactiveWebSocketHandler implements WebSocketHandler {
    
    private static final ObjectMapper json = new ObjectMapper();

    public static class Event {
    	UUID id;
    	String timestamp;

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public Event(UUID id, String timestamp) {
			super();
			this.id = id;
			this.timestamp = timestamp;
		}

		public Event() {
			super();
		}
    	
    }
    
    private Flux<String> eventFlux = Flux.generate(sink -> {
        Event event = new Event(UUID.randomUUID(), Instant.now().toString());
        try {
            sink.next(json.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            sink.error(e);
        }
    });

    private Flux<String> intervalFlux = Flux.interval(Duration.ofMillis(1000L))
      .zipWith(eventFlux, (time, event) -> event);

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession.send(intervalFlux
          .map(webSocketSession::textMessage))
          .and(webSocketSession.receive()
            .map(WebSocketMessage::getPayloadAsText)
            .map(s -> {System.out.println("Recieved: " + s);return s;})
            .log());
    }
}
