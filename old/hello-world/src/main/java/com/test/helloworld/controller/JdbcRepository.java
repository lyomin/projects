package com.test.helloworld.controller;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

public interface JdbcRepository extends Repository<Object, Long> {
	
	@Query()
	public List<Object> findAll();
}
