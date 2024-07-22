package com.demo.todoapp.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.todoapp.entity.Task;
import com.demo.todoapp.repository.TaskRepository;
import com.demo.todoapp.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository repository;

    @Override
    public Task createTask(String title, 
                            String description, 
                            boolean completed) {
        try {
            Task newTask = new Task(title, description, completed);
            return this.repository.save(newTask);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create Task");
        }
        
    }

    @Override
    public List<Task> getAllTasks() {
        return this.repository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        try {
            return this.repository.getReferenceById(id);
        } catch (Exception e) {
            throw new RuntimeException("Unable to find Task by " + id);
        }
    }

    @Override
    public Task updateTaskById(Long id, Task task) {
        try {
            Task existingTask = this.getTaskById(id);
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setCompleted(task.isCompleted());
            return this.repository.save(existingTask);
        } catch (Exception e) {
            throw new RuntimeException("Unable to update Task by " + id);
        }
    }

    @Override
    public void removeTaskById(Long id) {
        try {
            this.repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete Task by " + id);
        }
    }
}
