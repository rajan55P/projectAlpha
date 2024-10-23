package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.model.Task;
import com.example.demo.service.StudentService;
import com.example.demo.service.TaskService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private final StudentService studentService;


    @Autowired
    private TaskService taskService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/generate/{count}")
    public ResponseEntity<?> generateStudents(@PathVariable("count") int count) {

        // Generate a unique task ID for this request
        Task task = taskService.createTask();

        // Start the asynchronous task
        studentService.generateStudentsAsync(count, task.getTaskId());
        // Return task ID to the client for status tracking
        Map<String, String> response = new HashMap<>();
        response.put("message", "Generation started for " + count + " students.");
        response.put("taskId", task.getTaskId());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/paginatedStudents")
    public Page<Student> getFilteredStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) Integer startScore,
            @RequestParam(required = false) Integer endScore,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return studentService.getFilteredStudents(page, size, studentId, className, startScore, endScore, startDate, endDate);
    }

    @GetMapping("/students/export")
    public List<Student> exportAllStudents(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) Integer startScore,
            @RequestParam(required = false) Integer endScore,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return studentService.getFilteredStudentsWithoutPagination(studentId, className, startScore, endScore, startDate, endDate);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/task/{taskId}")
    public ResponseEntity<?> getTaskStatus(@PathVariable("count") int count) {

        // Generate a unique task ID for this request
        Task task = taskService.createTask();
        // Start the asynchronous task
        studentService.generateStudentsAsync(count, task.getTaskId());
        // Return task ID to the client for status tracking
        Map<String, String> response = new HashMap<>();
        response.put("message", "Generation started for " + count + " students.");
        response.put("taskId", task.getTaskId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}