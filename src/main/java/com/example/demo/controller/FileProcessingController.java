package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.model.Task;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.FileProcessingService;
import com.example.demo.service.TaskService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FileProcessingController {


    @Autowired
    private FileProcessingService fileProcessingService;

    @Autowired
    private TaskService taskService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/processFile")
    public ResponseEntity<?> processFile() {
        Map<String, String> response = new HashMap<>();
        try {
            String excelFilePath = "/Users/apple/Downloads/dataProcessing/projectAlpha/students.xlsx";
            String csvFilePath ="/Users/apple/Downloads/dataProcessing/projectAlpha/students.csv";
            Task task = taskService.createTask();

            fileProcessingService.processExcelAndSaveToCSV(excelFilePath, csvFilePath);
            response.put("message", "File processed and saved to CSV successfully!");
            response.put("taskId", task.getTaskId());


            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            System.out.println("Exception in processFile ="+ e);

            response.put("message", "Exception in processFile ="+ e);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/uploadData")
    public ResponseEntity<?> uploadStudentData() {
        Map<String, String> response = new HashMap<>();
        String excelFilePath = "/Users/apple/Downloads/dataProcessing/projectAlpha/students.xlsx";
        try {
            System.out.println("Inside uploadData");

            // Step 1: Read the Excel file
            fileProcessingService.readAndProcessExcelFile(excelFilePath);

//            List<Student> students = fileProcessingService.readAndProcessExcelFile(excelFilePath);

            // Step 2: Save the student records to the database
//            studentRepository.saveAll(students);

            response.put("message", "Student data uploaded and processed successfully.");

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            System.out.println("Exception in upload data ="+ e);
            response.put("message", "Student data uploaded and processed successfully.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

