package com.test.helloworld.controller.model;

public class UserDeleteRequest {
	long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserDeleteRequest(long id) {
		super();
		this.id = id;
	}

	public UserDeleteRequest() {
		super();
	}
	
}
