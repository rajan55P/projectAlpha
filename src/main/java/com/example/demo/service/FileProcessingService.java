package com.example.demo.service;

import com.example.demo.model.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        try {
            for (int i = 1; i < data.size(); i++) {  // Skipping the header row
                double score = Double.parseDouble(data.get(i)[5]);
                score += 10;
                data.get(i)[1] = String.valueOf(score);  // Update score in the data array
            }
        }
        catch (NumberFormatException e) {
            System.out.println("Error in updateStudentScores: Invalid number format. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error in updateStudentScores: " + e.getMessage());
        }
    }

    private void writeDataToCSV(List<String[]> data, String csvFilePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));
             PrintWriter csvWriter = new PrintWriter(writer)) {

            for (String[] row : data) {
                csvWriter.println(String.join(",", row));
            }
        }
        catch (Exception e){
            System.out.println("Error in writeDataToCSV =" + e);
        }
    }
    public List<Student> readAndProcessExcelFile(String excelFilePath) throws IOException {
        List<Student> students = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(excelFilePath))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                Student student = new Student();

                // Read Student ID
                if (row.getCell(0).getCellType() == CellType.NUMERIC) {
                    student.setStudentId((long) row.getCell(0).getNumericCellValue());
                } else {
                    // Handle the case when the ID is not numeric
                    System.out.println("Invalid student ID at row " + row.getRowNum());
                    continue;
                }

                // Read First Name
                if (row.getCell(1).getCellType() == CellType.STRING) {
                    student.setFirstName(row.getCell(1).getStringCellValue());
                } else {
                    System.out.println("Invalid first name at row " + row.getRowNum());
                    continue;
                }

                // Read Last Name
                if (row.getCell(2).getCellType() == CellType.STRING) {
                    student.setLastName(row.getCell(2).getStringCellValue());
                } else {
                    System.out.println("Invalid last name at row " + row.getRowNum());
                    continue;
                }

                // Read Date of Birth
                if (row.getCell(3).getCellType() == CellType.STRING) {
                    try {
                        String dateString = (row.getCell(3).getStringCellValue());
                        LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Adjust format as needed
                        student.setDateOfBirth(localDate);
                    } catch (DateTimeParseException e) {
                        System.out.println("Error parsing date for row " + row.getRowNum() + ": " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid date of birth at row " + row.getRowNum());
                    continue;
                }

                // Read Class Name
                if (row.getCell(4).getCellType() == CellType.STRING) {
                    student.setClassName(row.getCell(4).getStringCellValue());
                } else {
                    System.out.println("Invalid class name at row " + row.getRowNum());
                    continue;
                }

                // Read and Update Score
                if (row.getCell(5).getCellType() == CellType.NUMERIC) {
                    double score = row.getCell(5).getNumericCellValue() + 5; // Update the score by 5 points
                    student.setScore((int) score);
                } else {
                    System.out.println("Invalid score at row " + row.getRowNum());
                    continue;
                }

                students.add(student);
            }
        } catch (Exception e) {
            System.out.println("Error in readAndProcessExcelFile =" + e);
        }
        return students;
    }

}

