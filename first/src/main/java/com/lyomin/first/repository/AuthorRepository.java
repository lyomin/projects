package com.lyomin.first.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lyomin.first.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

}
