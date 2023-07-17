package com.test.helloworld.notification.domain;

import java.time.Instant;

public class UserRooms {
	
	Integer roomId;
	Long userId;
	//Instant updatedAt;
	
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public UserRooms(Integer roomId, Long userId) {
		super();
		this.roomId = roomId;
		this.userId = userId;
	}
	public UserRooms() {
		super();
	}
}
