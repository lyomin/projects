package com.example.task.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.domain.Task;
import com.example.task.repository.TasksRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("tasks")
public class TaskRepository {
	TasksRepository taskRepository;

	@Autowired
	public TaskRepository(TasksRepository taskRepository) {
		super();
		this.taskRepository = taskRepository;
	}
	
	@GetMapping("all")
	public Page<Task> getAll(Pageable pageable) {
		if (pageable.getSort().isUnsorted())
			pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.asc("dueDate")));
		return taskRepository.findAll(pageable);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Task> getItem(@PathVariable("id") Integer id) {
		return ResponseEntity.of(
				taskRepository.findById(id)
		);
	}
	
	@PostMapping()
	public ResponseEntity<Task> postTask(@RequestBody @Valid Task body) {
		if (body.getId() != null)
			if (!taskRepository.existsById(body.getId()))
				return ResponseEntity.notFound().build();
		return ResponseEntity.ok(
			taskRepository.save(body)
		);
	}
	
	@DeleteMapping("{id}")
	public void deleteItem(@PathVariable("id") Integer id) {
		taskRepository.deleteById(id);
	}
}	
