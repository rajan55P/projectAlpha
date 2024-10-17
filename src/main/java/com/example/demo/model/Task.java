package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Task {

    @Id
    private String taskId;

    private String status;
    private int progress;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String filePath;
    private String errorMessage;

    public Task(String taskId) {
        this.taskId = taskId;
        this.status = "in progress";  // Set default status
        this.progress = 0;            // Default progress at 0%
        this.startTime = LocalDateTime.now();  // Set the start time to current time
    }

    // Constructors, getters, setters, etc.

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // Method to mark task as completed
    public void completeTask(String filePath) {
        this.status = "completed";
        this.endTime = LocalDateTime.now();  // Set the end time to current time
        this.filePath = filePath;            // Store the file path
        this.progress = 100;                 // Mark progress as 100%
    }

    // Method to mark task as failed
    public void failTask(String errorMessage) {
        this.status = "failed";
        this.endTime = LocalDateTime.now();  // Set the end time to current time
        this.errorMessage = errorMessage;    // Store the error message
    }
}

