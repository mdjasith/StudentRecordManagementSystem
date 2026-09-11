package com.project.student_record_management.dto;

public class FeeDTO {

    private Long studentId;
    private Long courseId;
    private Double totalFee;
    private String dueDate;

    // ========== CONSTRUCTORS ==========
    public FeeDTO() {
    }

    public FeeDTO(Long studentId, Long courseId, Double totalFee, String dueDate) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.totalFee = totalFee;
        this.dueDate = dueDate;
    }

    // ========== GETTERS ==========
    public Long getStudentId() {
        return studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public String getDueDate() {
        return dueDate;
    }

    // ========== SETTERS ==========
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}