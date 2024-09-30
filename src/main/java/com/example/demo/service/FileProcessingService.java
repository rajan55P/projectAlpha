package com.example.demo.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileProcessingService {

    public void processExcelAndSaveToCSV(String excelFilePath, String csvFilePath) throws IOException {
        List<String[]> data = readExcelFile(excelFilePath);
        updateStudentScores(data);
        writeDataToCSV(data, csvFilePath);
    }

    private List<String[]> readExcelFile(String filePath) throws IOException {
        List<String[]> dataList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                String[] rowData = new String[row.getPhysicalNumberOfCells()];
                for (Cell cell : row) {
                    rowData[cell.getColumnIndex()] = cell.toString();
                }
                dataList.add(rowData);
            }
        }
        return dataList;
    }

    private void updateStudentScores(List<String[]> data) {
        for (int i = 1; i < data.size(); i++) {  // Skipping the header row
            int score = Integer.parseInt(data.get(i)[5]);
            score += 10;
            data.get(i)[1] = String.valueOf(score);  // Update score in the data array
        }
    }

    private void writeDataToCSV(List<String[]> data, String csvFilePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));
             PrintWriter csvWriter = new PrintWriter(writer)) {

            for (String[] row : data) {
                csvWriter.println(String.join(",", row));
            }
        }
    }
}

