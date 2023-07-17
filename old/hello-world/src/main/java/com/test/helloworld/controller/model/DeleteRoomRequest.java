package com.test.helloworld.controller.model;

public class DeleteRoomRequest {
	Integer id;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public DeleteRoomRequest(Integer id) {
		super();
		this.id = id;
	}
	public DeleteRoomRequest() {
		super();
	}
}
