package com.project.student_record_management.dto;

import jakarta.validation.constraints.NotNull;

public class EnrollmentDTO {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Semester is required")
    private String semester;

    // ========== CONSTRUCTORS ==========
    public EnrollmentDTO() {
    }

    public EnrollmentDTO(Long studentId, Long courseId, String semester) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.semester = semester;
    }

    // ========== GETTERS ==========
    public Long getStudentId() {
        return studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getSemester() {
        return semester;
    }

    // ========== SETTERS ==========
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}