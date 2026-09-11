package com.project.student_record_management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String courseCode;

    @Column(nullable = false)
    private String courseName;

    private Integer credits;

    private String professorName;

    // ========== CONSTRUCTORS ==========
    public Course() {
    }

    public Course(Long id, String courseCode, String courseName, Integer credits, String professorName) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.professorName = professorName;
    }

    // ========== GETTERS ==========
    public Long getId() {
        return id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public Integer getCredits() {
        return credits;
    }

    public String getProfessorName() {
        return professorName;
    }

    // ========== SETTERS ==========
    public void setId(Long id) {
        this.id = id;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }
}