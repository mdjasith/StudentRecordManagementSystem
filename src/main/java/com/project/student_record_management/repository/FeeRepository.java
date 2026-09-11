package com.project.student_record_management.repository;

import com.project.student_record_management.entity.Fee;
import com.project.student_record_management.entity.Student;
import com.project.student_record_management.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {

    // Find fee by student
    Optional<Fee> findByStudent(Student student);

    // Find fee by student and course
    Optional<Fee> findByStudentAndCourse(Student student, Course course);

    // Check if fee exists for student
    boolean existsByStudent(Student student);

    // Check if fee exists for student and course
    boolean existsByStudentAndCourse(Student student, Course course);

    // Find fees by status
    List<Fee> findByStatus(String status);
}