package com.example.demo.service;

import com.example.demo.model.Student;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.springframework.scheduling.annotation.Async;
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

    private static final int BATCH_SIZE = 1000;  // Set batch size for processing chunks

    @Async
    public void processExcelAndSaveToCSV(String excelFilePath, String csvFilePath) throws IOException {
        try (InputStream is = new FileInputStream(excelFilePath);
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)    // Number of rows to keep in memory
                     .bufferSize(4096)     // Buffer size for reading input stream
                     .open(is);            // Open the input stream
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));
             PrintWriter csvWriter = new PrintWriter(writer)) {

            // Iterate through the sheets in the workbook
            for (Sheet sheet : workbook) {
                boolean isHeaderProcessed = false; // Flag to check if header is processed

                // Process rows
                for (Row row : sheet) {  // Iterate over rows directly
                    if (!isHeaderProcessed) {
                        // Write CSV header based on the first row
                        csvWriter.println(rowToCSV(row));
                        isHeaderProcessed = true; // Set flag to true after processing header
                        continue; // Skip to the next row
                    }

                    // Process the row data
//                  String[] rowData = rowToArray(row);
//                    updateStudentScore(row); // Process and update student score
                    csvWriter.println(rowToCSV(row));

                    // Flush data to CSV file after processing a batch
                    if (row.getRowNum() % BATCH_SIZE == 0) {
                        csvWriter.flush();
                    }
                }
                csvWriter.flush();  // Flush the remaining data after processing the sheet
            }
        } catch (Exception e) {
            System.err.println("Error processing Excel file: " + e.getMessage());
            e.printStackTrace(); // Consider using a logging framework
        }
    }

    private void saveWorkbook(String excelFilePath) {
        try (FileOutputStream fos = new FileOutputStream(excelFilePath, false)) {
            // Write changes to the workbook
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096)
                    .open(new FileInputStream(excelFilePath)); // Open existing workbook
            workbook.write(fos);
        } catch (IOException e) {
            System.err.println("Error saving the workbook: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    // Converts a row to CSV format (String)
    // Converts a row to CSV format (String) and updates the score
    private String rowToCSV(Row row) {
        StringBuilder sb = new StringBuilder();
        for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) {
            Cell cell = row.getCell(cellIndex);  // Get the cell at the current index
            if (sb.length() > 0) sb.append(",");  // Add comma before appending cell data if not the first cell

            // Check the cell index and retrieve the value accordingly
            switch (cellIndex) {
                case 0: // Student ID
                    sb.append((long) cell.getNumericCellValue()); // Assuming ID is numeric
                    break;
                case 1: // First Name
                    sb.append(cell.getStringCellValue());
                    break;
                case 2: // Last Name
                    sb.append(cell.getStringCellValue());
                    break;
                case 3: // Score
                    sb.append((int) cell.getNumericCellValue() + 10); // Assuming score is numeric
                    break;
                case 4: // Date of Birth
                    sb.append(cell.getStringCellValue()); // Assuming it's a string or format as needed
                    break;
                case 5: // Class Name
                    sb.append(cell.getStringCellValue());
                    break;
                default:
                    sb.append("Unsupported cell index: " + cellIndex);
            }
        }
        return sb.toString();  // Return the CSV line
    }


    // Converts a row into a String array for easier manipulation
    private String[] rowToArray(Row row) {
        int cellCount = row.getPhysicalNumberOfCells();
        String[] rowData = new String[cellCount];
        for (Cell cell : row) {
            rowData[cell.getColumnIndex()] = cell.toString();
        }
        return rowData;
    }

    // Updates the score in the row (assumes the score is in column index 5)
    private void updateStudentScore(Row row) {
        try {
            Cell scoreCell = row.getCell(3); // Assuming the score is now in the 4th column (index 3)

            if (scoreCell != null) { // Check if the score cell is not null
                if (scoreCell.getCellType() == CellType.NUMERIC) { // Ensure the cell type is numeric
//                    double score = scoreCell.getNumericCellValue();
//                    score += 10;  // Increment score by 10 points
//                    scoreCell.setCellValue(score); // Update the score in the cell
                    //  saveWorkbook(excelFilePath);
                } else {
                    // Assuming the student identifier is in the same cell as the score
                    String studentIdentifier = (row.getCell(3) != null) ? row.getCell(3).getStringCellValue() : "Unknown";
                    System.err.println("Invalid score format for student: " + studentIdentifier);
                }
            } else {
                // Assuming the student identifier is in the same cell as the score
                String studentIdentifier = (row.getCell(3) != null) ? row.getCell(3).getStringCellValue() : "Unknown";
                System.err.println("Score cell is null for student: " + studentIdentifier);
            }
        } catch (Exception e) {
            // Safely handle the case where the cell cannot be converted to a string
            System.out.println("cell Type= "+row.getCell(3).getCellType());
            String studentIdentifier = (row.getCell(3) != null) ? row.getCell(3).getStringCellValue() : "Unknown";
            System.err.println("Error updating score for student: " + studentIdentifier + ", " + e.getMessage());
        }
    }





    // Method to read and process Excel data into a list of students
    public List<Student> readAndProcessExcelFile(String excelFilePath) throws IOException {
        List<Student> students = new ArrayList<>();
        try (InputStream is = new FileInputStream(excelFilePath);
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)
                     .bufferSize(4096)
                     .open(is)) {
            for (Sheet sheet : workbook) {
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Start from the second row
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        Student student = extractStudentFromRow(row);
                        if (student != null) {
                            students.add(student);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in readAndProcessExcelFile: " + e.getMessage());
            e.printStackTrace(); // Consider using a logging framework
        }
        return students;
    }

    // Extracts Student data from an Excel row
    private Student extractStudentFromRow(Row row) {
        try {
            Student student = new Student();

            // Read and validate Student ID
            if (row.getCell(0).getCellType() == CellType.NUMERIC) {
                student.setStudentId((long) row.getCell(0).getNumericCellValue());
            } else {
                System.err.println("Invalid student ID at row " + row.getRowNum());
                return null;
            }

            // Read and validate First Name
            if (row.getCell(1).getCellType() == CellType.STRING) {
                student.setFirstName(row.getCell(1).getStringCellValue());
            } else {
                System.err.println("Invalid first name at row " + row.getRowNum());
                return null;
            }

            // Read and validate Last Name
            if (row.getCell(2).getCellType() == CellType.STRING) {
                student.setLastName(row.getCell(2).getStringCellValue());
            } else {
                System.err.println("Invalid last name at row " + row.getRowNum());
                return null;
            }

            // Read and parse Date of Birth
            if (row.getCell(3).getCellType() == CellType.STRING) {
                try {
                    String dateString = row.getCell(3).getStringCellValue();
                    LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    student.setDateOfBirth(localDate);
                } catch (DateTimeParseException e) {
                    System.err.println("Error parsing date for row " + row.getRowNum() + ": " + e.getMessage());
                    return null;
                }
            }

            // Read Class Name
            if (row.getCell(4).getCellType() == CellType.STRING) {
                student.setClassName(row.getCell(4).getStringCellValue());
            }

            // Update and validate Score
            if (row.getCell(5).getCellType() == CellType.NUMERIC) {
                double score = row.getCell(5).getNumericCellValue() + 5;
                student.setScore((int) score);
            }

            return student;
        } catch (Exception e) {
            System.err.println("Error extracting student data from row " + row.getRowNum() + ": " + e.getMessage());
            return null;
        }
    }
}