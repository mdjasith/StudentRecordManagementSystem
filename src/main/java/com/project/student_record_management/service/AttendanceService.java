package com.project.student_record_management.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.student_record_management.entity.Attendance;
import com.project.student_record_management.entity.Enrollment;
import com.project.student_record_management.entity.Student;
import com.project.student_record_management.repository.AttendanceRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    // Mark Attendance
    public Attendance markAttendance(Long enrollmentId, String status) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);
        
        LocalDate today = LocalDate.now();
        if (attendanceRepository.existsByEnrollmentAndDate(enrollment, today)) {
            throw new RuntimeException("Attendance already marked for today!");
        }
        
        Attendance attendance = new Attendance();
        attendance.setEnrollment(enrollment);
        attendance.setDate(today);
        attendance.setStatus(status.toUpperCase());
        
        return attendanceRepository.save(attendance);
    }

    // Get Attendance by Student
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        // First get the student entity
        Student student = studentService.getStudentEntity(studentId);
        
        // Then get all enrollments for this student
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        
        // Collect all attendance records
        List<Attendance> attendanceList = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            attendanceList.addAll(attendanceRepository.findByEnrollment(enrollment));
        }
        
        return attendanceList;
    }

    // Get Attendance Percentage
    public double getAttendancePercentage(Long studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        
        if (enrollments.isEmpty()) {
            return 0.0;
        }
        
        long totalPresent = 0;
        long totalClasses = 0;
        
        for (Enrollment enrollment : enrollments) {
            totalPresent += attendanceRepository.countPresentByEnrollment(enrollment);
            totalClasses += attendanceRepository.countTotalByEnrollment(enrollment);
        }
        
        if (totalClasses == 0) {
            return 0.0;
        }
        
        return (double) totalPresent / totalClasses * 100;
    }

    // Get Attendance by Enrollment
    public List<Attendance> getAttendanceByEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);
        return attendanceRepository.findByEnrollment(enrollment);
    }
}