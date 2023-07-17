package com.test.helloworld.notification.domain;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Table(name = "messages")
public class Message {
	@Id
	Long id;
	@Column(value = "room_id")
	Integer roomId;
	String message;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	Instant createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	Instant updatedAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Message(Long id, Integer roomId, String message, Instant createdAt, Instant updatedAt) {
		super();
		this.id = id;
		this.roomId = roomId;
		this.message = message;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public Message() {
		super();
	}
}
