package com.example.demo.controller;

import com.example.demo.service.FileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class FileProcessingController {

    @Autowired
    private FileProcessingService fileProcessingService;
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/processFile")
    public String processFile() {
        try {
            String excelFilePath = "/Users/apple/Downloads/dataProcessing/projectAlpha/students.xlsx";
            String csvFilePath ="/Users/apple/Downloads/dataProcessing/projectAlpha/students.csv";
            fileProcessingService.processExcelAndSaveToCSV(excelFilePath, csvFilePath);
            return "File processed and saved to CSV successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

