package com.test.helloworld.notification.domain;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Table("users")
public class Users {
	@Id
	Long id;
	String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	Instant respondedAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	Instant lastMsgAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Instant getRespondedAt() {
		return respondedAt;
	}
	public void setRespondedAt(Instant respondedAt) {
		this.respondedAt = respondedAt;
	}
	public Instant getLastMsgAt() {
		return lastMsgAt;
	}
	public void setLastMsgAt(Instant lastMsgAt) {
		this.lastMsgAt = lastMsgAt;
	}
	public Users(Long id, String name, Instant respondedAt, Instant lastMsgAt) {
		super();
		this.id = id;
		this.name = name;
		this.respondedAt = respondedAt;
		this.lastMsgAt = lastMsgAt;
	}
	public Users() {
		super();
	}
}
