package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/generate/{count}")
    public ResponseEntity<?> generateStudents(@PathVariable("count") int count) throws IOException {
        try {
            List<Student> students = studentService.generateStudents(count);
            // studentService.saveStudents(students);

            // Generate Excel file
            String filePath = "/Users/apple/Downloads/dataProcessing/projectAlpha/students.xlsx";
            generateExcelFile(students, filePath);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Generated " + count + " student records and saved to Excel.");
            System.out.println("Inside the method");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
        catch (Exception e){
            return ResponseEntity.ok("Error occurred error="+e);
        }
    }


    private void generateExcelFile(List<Student> students, String filePath) throws IOException {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Students");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Student ID", "First Name", "Last Name", "DOB", "Class", "Score"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Populate data rows
            int rowIdx = 1;
            for (Student student : students) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(student.getStudentId());
                row.createCell(1).setCellValue(student.getFirstName());
                row.createCell(2).setCellValue(student.getLastName());
                row.createCell(3).setCellValue(student.getDateOfBirth().toString()); // assuming DOB is a LocalDate
                row.createCell(4).setCellValue(student.getClassName());
                row.createCell(5).setCellValue(student.getScore());
            }

            // Create directories if they do not exist
            Files.createDirectories(Paths.get(filePath).getParent());

            // Write to Excel file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            workbook.close();
        } catch (IOException e) {
            logger.error("Error occurred while generating Excel file: {}", e.getMessage());
            // Optional: rethrow the exception or handle it as needed
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
            // Optional: rethrow the exception or handle it as needed
        }
    }
}