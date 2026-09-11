package com.project.student_record_management.repository;

import com.project.student_record_management.entity.Enrollment;
import com.project.student_record_management.entity.Student;
import com.project.student_record_management.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudent(Student student);

    List<Enrollment> findByCourse(Course course);

    boolean existsByStudentAndCourse(Student student, Course course);

    // ✅ Count enrollments by student
    long countByStudent(Student student);
}