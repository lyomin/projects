package com.test.helloworld.controller.model;

public class NewRequest {
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public NewRequest(String name) {
		super();
		this.name = name;
	}

	public NewRequest() {
		super();
	}
}
