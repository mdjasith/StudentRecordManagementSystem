package com.project.student_record_management.repository;

import com.project.student_record_management.entity.Attendance;
import com.project.student_record_management.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    // Find attendance by enrollment
    List<Attendance> findByEnrollment(Enrollment enrollment);
    
    // ✅ THIS METHOD WAS MISSING - Check if attendance exists for enrollment on a specific date
    boolean existsByEnrollmentAndDate(Enrollment enrollment, LocalDate date);
    
    // Count present days for a student in a course
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.enrollment = :enrollment AND a.status = 'PRESENT'")
    long countPresentByEnrollment(@Param("enrollment") Enrollment enrollment);
    
    // Count total attendance days for a student in a course
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.enrollment = :enrollment")
    long countTotalByEnrollment(@Param("enrollment") Enrollment enrollment);
}