package com.demo.todoapp.service;

import java.util.List;
import com.demo.todoapp.entity.Task;

public interface TaskService {
    Task createTask(String title, 
                    String description, 
                    boolean completed);
    List<Task> getAllTasks();
    Task getTaskById(Long id);
    Task updateTaskById(Long id, Task task);
    void removeTaskById(Long id); 
}
