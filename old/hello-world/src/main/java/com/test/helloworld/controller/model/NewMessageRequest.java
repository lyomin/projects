package com.test.helloworld.controller.model;

import org.springframework.lang.NonNull;

public class NewMessageRequest {
	
	
	private @NonNull Integer room;
	private @NonNull String message;
	private @NonNull String userName;
	

	public Integer getRoom() {
		return room;
	}
	public void setRoom(Integer room) {
		this.room = room;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public NewMessageRequest(Integer room, String message,String userName) {
		super();
		this.room = room;
		this.message = message;
		this.userName = userName;
	}
	public NewMessageRequest() {
		super();
	}
	@Override
	public String toString() {
		return "NewMessageRequest [room=" + room + ", message=" + message + ", userName=" + userName + "]";
	}
}
