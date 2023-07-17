package com.example.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.task.domain.Task;

public interface TasksRepository extends JpaRepository<Task, Integer>{

}
