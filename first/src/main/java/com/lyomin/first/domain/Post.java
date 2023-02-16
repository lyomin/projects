package com.lyomin.first.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Data
@Getter
@Setter
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String title;
	String body;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Column(nullable = false)
	public Instant createdAt;

	@JsonCreator
	public Post(Integer id, String title, String body) {
		super();
		this.id = id;
		this.title = title;
		this.body = body;
		this.createdAt = null;
	}
}
