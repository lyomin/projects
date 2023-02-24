package com.lyomin.first.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Getter
@Setter
@Entity
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Integer id;
	String author;
}
