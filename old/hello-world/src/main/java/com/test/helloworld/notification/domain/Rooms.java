package com.test.helloworld.notification.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Rooms {
	@Id
	private Integer id;
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Rooms(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Rooms() {
		super();
	}
}
