package com.example.demo.service;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
            student.setFirstName(randomString(3, 8));
            student.setLastName(randomString(3, 8));
            student.setDateOfBirth(generateRandomDate());
            student.setClassName(CLASS_NAMES[random.nextInt(CLASS_NAMES.length)]);
            student.setScore(55 + random.nextInt(31)); // Random score between 55 and 85

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
}
