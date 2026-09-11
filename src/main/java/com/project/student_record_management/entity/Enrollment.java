package com.project.student_record_management.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String semester;

    private LocalDate enrollmentDate = LocalDate.now();

    // ========== CONSTRUCTORS ==========
    public Enrollment() {
    }

    public Enrollment(Long id, Student student, Course course, String semester, LocalDate enrollmentDate) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.semester = semester;
        this.enrollmentDate = enrollmentDate;
    }

    // ========== GETTERS ==========
    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public String getSemester() {
        return semester;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    // ========== SETTERS ==========
    public void setId(Long id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}