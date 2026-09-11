package com.project.student_record_management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fees")
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false)
    private Double totalFee;

    private Double amountPaid = 0.0;

    @Column(nullable = false)
    private String status;

    private String paymentDate;

    private String dueDate;

    // ========== CONSTRUCTORS ==========
    public Fee() {
    }

    public Fee(Long id, Student student, Course course, Double totalFee, 
               Double amountPaid, String status, String paymentDate, String dueDate) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.totalFee = totalFee;
        this.amountPaid = amountPaid;
        this.status = status;
        this.paymentDate = paymentDate;
        this.dueDate = dueDate;
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

    public Double getTotalFee() {
        return totalFee;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getDueDate() {
        return dueDate;
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

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}