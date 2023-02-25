package com.lyomin.first.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	@Column(columnDefinition = "TEXT")
	String content;
	
	@OneToMany(mappedBy = "post")
	List<Section> parts;
	
	String shortContent;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id")
	Author publishedBy;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Column(nullable = false)
	Instant createdAt;
	
	boolean visible;
	boolean approved;
	
	boolean blocked;
}
