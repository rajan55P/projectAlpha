package com.example.demo.service;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    private static final String[] CLASS_NAMES = {"Class1", "Class2", "Class3", "Class4", "Class5"};
    private Random random = new Random();

    // Method to generate student data
    public List<Student> generateStudents(int count) {
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Student student = new Student();
            student.setStudentId(random.nextLong());
            student.setFirstName(randomString(3, 8));
            student.setLastName(randomString(3, 8));
            student.setScore(55 + random.nextInt(31)); // Random score between 55 and 85
            student.setDateOfBirth(generateRandomDate());
            student.setClassName(CLASS_NAMES[random.nextInt(CLASS_NAMES.length)]);
            students.add(student);
        }

        return students;
    }

    // Helper method to generate random string
    private String randomString(int min, int max) {
        int length = min + random.nextInt(max - min + 1);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26))); // Lowercase letters
        }
        return sb.toString();
    }

    // Helper method to generate random date
    private LocalDate generateRandomDate() {
        int year = random.nextInt(11) + 2000; // Between 2000 and 2010
        int month = random.nextInt(12) + 1; // Between 1 and 12
        int day = random.nextInt(28) + 1; // Between 1 and 28
        return LocalDate.of(year, month, day);
    }

    // Method to save students to the database
    public void saveStudents(List<Student> students) {
        studentRepository.saveAll(students);
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    private Map<String, String> taskStatus = new ConcurrentHashMap<>();

    @Async
    public CompletableFuture<String> generateStudentsAsync(int count, String taskId) {
        try {
            int batchSize = 100000;
            String filePath = "/Users/apple/Downloads/dataProcessing/projectAlpha/students.xlsx";
//          String filePath = "/Users/apple/Downloads/dataProcessing/projectAlpha/students_" + taskId + ".xlsx";

            // Initialize Excel writer
            try (FileOutputStream fos = new FileOutputStream(filePath);
                 SXSSFWorkbook workbook = new SXSSFWorkbook()) {

                Sheet sheet = workbook.createSheet("Students");
                int rowCount = 0;

                // Process students in batches
                for (int i = 0; i < count; i += batchSize) {
                    int limit = Math.min(batchSize, count - i);
                    List<Student> studentsBatch = generateStudents(limit);
                    rowCount = writeStudentsToSheet(sheet, studentsBatch, rowCount);
                    studentsBatch.clear();
                }

                workbook.write(fos);
                taskStatus.put(taskId, "completed");
            }

        } catch (Exception e) {
            taskStatus.put(taskId, "failed: " + e.getMessage());
        }

        return CompletableFuture.completedFuture(taskId);
    }

    public String getTaskStatus(String taskId) {
        return taskStatus.getOrDefault(taskId, "not_found");
    }

    private int writeStudentsToSheet(Sheet sheet, List<Student> studentsBatch, int startRow) {
        int rowNum = startRow;
        for (Student student : studentsBatch) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getStudentId());
            row.createCell(1).setCellValue(student.getFirstName());
            row.createCell(2).setCellValue(student.getLastName());
            row.createCell(3).setCellValue(student.getScore());
            row.createCell(4).setCellValue(student.getDateOfBirth());
            row.createCell(5).setCellValue(student.getClassName());
        }
        return rowNum;
    }


}
