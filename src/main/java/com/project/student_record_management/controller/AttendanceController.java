package com.project.student_record_management.controller;

import com.project.student_record_management.entity.Attendance;
import com.project.student_record_management.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:3000")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/mark/{enrollmentId}")
    public ResponseEntity<Attendance> markAttendance(@PathVariable Long enrollmentId, @RequestParam String status) {
        return new ResponseEntity<>(attendanceService.markAttendance(enrollmentId, status), HttpStatus.CREATED);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId));
    }

    @GetMapping("/percentage/{studentId}")
    public ResponseEntity<Double> getAttendancePercentage(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendancePercentage(studentId));
    }
}