package com.project.student_record_management.service;

import com.project.student_record_management.dto.FeeDTO;
import com.project.student_record_management.entity.Fee;
import com.project.student_record_management.entity.Student;
import com.project.student_record_management.entity.Course;
import com.project.student_record_management.exception.ResourceNotFoundException;
import com.project.student_record_management.repository.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeeService {

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    // ✅ CREATE FEE (Manual)
    public Fee createFee(FeeDTO feeDTO) {
        Student student = studentService.getStudentEntity(feeDTO.getStudentId());

        Course course = null;
        if (feeDTO.getCourseId() != null) {
            course = courseService.getCourseById(feeDTO.getCourseId());
        }

        if (feeRepository.existsByStudent(student)) {
            throw new RuntimeException("Fee record already exists for this student! Use update instead.");
        }

        Fee fee = new Fee();
        fee.setStudent(student);
        fee.setCourse(course);
        fee.setTotalFee(feeDTO.getTotalFee());
        fee.setAmountPaid(0.0);
        fee.setStatus("UNPAID");
        fee.setDueDate(feeDTO.getDueDate());

        return feeRepository.save(fee);
    }

    // ✅ AUTO-CREATE OR UPDATE FEE FOR ENROLLMENT
    @Transactional
    public Fee createOrUpdateFeeForEnrollment(Student student, Course course) {
        // Calculate fee for this course
        double courseFee = calculateCourseFee(course);
        
        // Check if student already has a fee record
        Fee existingFee = feeRepository.findByStudent(student).orElse(null);
        
        if (existingFee != null) {
            // ✅ UPDATE EXISTING FEE - Add new course fee to total
            double newTotal = existingFee.getTotalFee() + courseFee;
            existingFee.setTotalFee(newTotal);
            
            // ✅ If fee was PAID, status should become PARTIAL (since new fee added)
            if (existingFee.getStatus().equals("PAID")) {
                existingFee.setStatus("PARTIAL");
            }
            
            // ✅ Update due date (extend by 30 days)
            existingFee.setDueDate(LocalDate.now().plusDays(30).toString());
            
            Fee updatedFee = feeRepository.save(existingFee);
            System.out.println("✅ Updated fee record for student: " + student.getEmail() +
                    " | New Total: ₹" + newTotal);
            return updatedFee;
            
        } else {
            // ✅ CREATE NEW FEE RECORD
            Fee newFee = new Fee();
            newFee.setStudent(student);
            newFee.setCourse(course);
            newFee.setTotalFee(courseFee);
            newFee.setAmountPaid(0.0);
            newFee.setStatus("UNPAID");
            newFee.setDueDate(LocalDate.now().plusDays(30).toString());

            Fee savedFee = feeRepository.save(newFee);
            System.out.println("✅ Auto-created fee record for student: " + student.getEmail() +
                    " | Amount: ₹" + courseFee);
            return savedFee;
        }
    }

    // ✅ CALCULATE COURSE FEE
    private double calculateCourseFee(Course course) {
        // Fixed fee per course
        return 5000.0;
        
        // OR dynamic based on credits:
        // return course.getCredits() * 1500.0;
    }

    // ✅ PAY FEE
    public Fee payFee(Long feeId, Double paymentAmount) {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fee record not found"));

        double newPaid = fee.getAmountPaid() + paymentAmount;
        fee.setAmountPaid(newPaid);
        fee.setPaymentDate(LocalDate.now().toString());

        if (newPaid >= fee.getTotalFee()) {
            fee.setStatus("PAID");
        } else if (newPaid > 0) {
            fee.setStatus("PARTIAL");
        }

        return feeRepository.save(fee);
    }

    // ✅ GET FEE BY STUDENT
    public Fee getFeeByStudentId(Long studentId) {
        Student student = studentService.getStudentEntity(studentId);
        return feeRepository.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Fee record not found for this student"));
    }

    // ✅ GET UNPAID FEES
    public List<Fee> getUnpaidFees() {
        return feeRepository.findByStatus("UNPAID");
    }

    // ✅ GET PARTIAL FEES
    public List<Fee> getPartialFees() {
        return feeRepository.findByStatus("PARTIAL");
    }

    // ✅ CHECK IF FEE IS PAID
    public boolean isFeePaid(Long studentId) {
        Student student = studentService.getStudentEntity(studentId);
        Fee fee = feeRepository.findByStudent(student).orElse(null);
        if (fee == null) return false;
        return fee.getStatus().equals("PAID");
    }

    // ✅ GET ALL FEES
    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }

    // ✅ DELETE FEE
    public void deleteFee(Long id) {
        if (!feeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fee record not found with id: " + id);
        }
        feeRepository.deleteById(id);
    }
}