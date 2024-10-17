package com.example.demo.service;

import com.example.demo.model.Task;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {

    private Map<String, Task> taskMap = new ConcurrentHashMap<>();

    public Task createTask() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(taskId);
        taskMap.put(taskId, task);
        return task;
    }

    public Task getTask(String taskId) {
        return taskMap.get(taskId);
    }

    public void updateTaskProgress(String taskId, int progress) {
        Task task = taskMap.get(taskId);
        if (task != null) {
            task.setProgress(progress);
        }
    }

    public void completeTask(String taskId, String filePath) {
        Task task = taskMap.get(taskId);
        if (task != null) {
            task.completeTask(filePath);
        }
    }

    public void failTask(String taskId, String errorMessage) {
        Task task = taskMap.get(taskId);
        if (task != null) {
            task.failTask(errorMessage);
        }
    }
}
