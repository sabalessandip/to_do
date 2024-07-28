package com.demo.todoapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.demo.todoapp.entity.Task;
import com.demo.todoapp.service.TaskService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class TaskController {
    @Autowired
    TaskService service;

    @GetMapping("/")
    public ResponseEntity<String> defaultRoute() {
         return ResponseEntity.ok().body("service is running");
    }

    @GetMapping("/health")
    public ResponseEntity<String> checkServiceHealth() {
         return ResponseEntity.ok().body("service is healthy");
    }

    @GetMapping("/tasks")
    public List<Task> getAllTask() {
        return this.service.getAllTasks();
    }
    
    @PostMapping("/task")
    public Task createTask(@RequestBody Task task) {
        return this.service.createTask(task.getTitle(), task.getDescription(), task.isCompleted());
    }

    @PutMapping("task/{id}")
    public Task updateTask(@PathVariable("id") Long id, @RequestBody Task task) {        
        return this.service.updateTaskById(id, task);
    }

    @DeleteMapping("task/{id}")
    public void deleteTask(@PathVariable("id") Long id) {
        this.service.removeTaskById(id);
    }
}
