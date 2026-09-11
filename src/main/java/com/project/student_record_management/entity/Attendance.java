package com.project.student_record_management.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String status;  // "PRESENT" or "ABSENT"

    // ========== CONSTRUCTORS ==========
    public Attendance() {
    }

    public Attendance(Long id, Enrollment enrollment, LocalDate date, String status) {
        this.id = id;
        this.enrollment = enrollment;
        this.date = date;
        this.status = status;
    }

    // ========== GETTERS ==========
    public Long getId() {
        return id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    // ========== SETTERS ==========
    public void setId(Long id) {
        this.id = id;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}