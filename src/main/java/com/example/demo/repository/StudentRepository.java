package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByFirstName(String firstName);
    Optional<Student> findByDateOfBirth(LocalDate dateOfBirth);

    @Query("SELECT s FROM Student s WHERE "
            + "(:studentId IS NULL OR s.studentId = :studentId) AND "
            + "(:className IS NULL OR s.className LIKE %:className%) AND "
            + "(:startScore IS NULL OR s.score >= :startScore) AND "
            + "(:endScore IS NULL OR s.score <= :endScore) AND "
            + "(:startDate IS NULL OR s.dateOfBirth >= :startDate) AND "
            + "(:endDate IS NULL OR s.dateOfBirth <= :endDate)")
    Page<Student> findFilteredStudents(Long studentId, String className, Integer startScore, Integer endScore, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE "
            + "(:studentId IS NULL OR s.studentId = :studentId) AND "
            + "(:className IS NULL OR s.className LIKE %:className%) AND "
            + "(:startScore IS NULL OR s.score >= :startScore) AND "
            + "(:endScore IS NULL OR s.score <= :endScore) AND "
            + "(:startDate IS NULL OR s.dateOfBirth >= :startDate) AND "
            + "(:endDate IS NULL OR s.dateOfBirth <= :endDate)")
    List<Student> findFilteredStudentsWithoutPagination(Long studentId, String className, Integer startScore, Integer endScore, LocalDate startDate, LocalDate endDate);

}
