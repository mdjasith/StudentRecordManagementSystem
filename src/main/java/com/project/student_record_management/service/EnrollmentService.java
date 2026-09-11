package com.project.student_record_management.service;

import com.project.student_record_management.dto.EnrollmentDTO;
import com.project.student_record_management.entity.Enrollment;
import com.project.student_record_management.entity.Student;
import com.project.student_record_management.entity.Course;
import com.project.student_record_management.entity.Fee;
import com.project.student_record_management.exception.ResourceNotFoundException;
import com.project.student_record_management.repository.EnrollmentRepository;
import com.project.student_record_management.repository.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeRepository feeRepository;  // ✅ ADD THIS

    // ✅ GET ALL ENROLLMENTS
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    // ✅ ENROLL STUDENT WITH AUTO-FEE (UPDATE OR CREATE)
    @Transactional
    public Enrollment enrollStudent(EnrollmentDTO enrollmentDTO) {
        // Step 1: Validate and fetch student
        Student student = studentService.getStudentEntity(enrollmentDTO.getStudentId());
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + enrollmentDTO.getStudentId());
        }

        // Step 2: Validate and fetch course
        Course course = courseService.getCourseById(enrollmentDTO.getCourseId());
        if (course == null) {
            throw new RuntimeException("Course not found with ID: " + enrollmentDTO.getCourseId());
        }

        // Step 3: Check for duplicate enrollment
        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new RuntimeException("Student is already enrolled in this course!");
        }

        // Step 4: Create and save enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setSemester(enrollmentDTO.getSemester());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // ✅ Step 5: CREATE OR UPDATE FEE RECORD
        try {
            feeService.createOrUpdateFeeForEnrollment(student, course);
            System.out.println("✅ Fee record processed for student: " + student.getEmail());
        } catch (Exception e) {
            System.err.println("❌ Failed to process fee record: " + e.getMessage());
            // Don't throw exception - enrollment is still valid
        }

        return savedEnrollment;
    }

    // ✅ GET ENROLLMENTS BY STUDENT
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        Student student = studentService.getStudentEntity(studentId);
        return enrollmentRepository.findByStudent(student);
    }

    // ✅ GET ENROLLMENTS BY COURSE
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        Course course = courseService.getCourseById(courseId);
        return enrollmentRepository.findByCourse(course);
    }

    // ✅ GET ENROLLMENT BY ID
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
    }

    // ✅ DELETE ENROLLMENT
    @Transactional
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));

        // Check if student has other enrollments
        long enrollmentCount = enrollmentRepository.countByStudent(enrollment.getStudent());

        if (enrollmentCount <= 1) {
            // If this is the only enrollment, delete fee too
            try {
                Fee fee = feeService.getFeeByStudentId(enrollment.getStudent().getId());
                if (fee != null) {
                    feeService.deleteFee(fee.getId());
                    System.out.println("✅ Deleted fee record for student: " + enrollment.getStudent().getEmail());
                }
            } catch (Exception e) {
                System.out.println("No fee record found to delete for student: " + enrollment.getStudent().getEmail());
            }
        } else {
            // ✅ If student has other enrollments, reduce fee
            try {
                Fee fee = feeService.getFeeByStudentId(enrollment.getStudent().getId());
                if (fee != null) {
                    double courseFee = 5000.0; // Same as calculateCourseFee in FeeService
                    double newTotal = fee.getTotalFee() - courseFee;
                    
                    // Ensure fee doesn't go below 0
                    if (newTotal < 0) {
                        newTotal = 0.0;
                    }
                    
                    fee.setTotalFee(newTotal);
                    
                    // Update status based on new total and paid amount
                    if (fee.getAmountPaid() >= newTotal && newTotal > 0) {
                        fee.setStatus("PAID");
                    } else if (fee.getAmountPaid() > 0 && newTotal > 0) {
                        fee.setStatus("PARTIAL");
                    } else if (newTotal == 0) {
                        fee.setStatus("PAID");
                        fee.setTotalFee(0.0);
                        fee.setAmountPaid(0.0);
                    } else {
                        fee.setStatus("UNPAID");
                    }
                    
                    feeRepository.save(fee);
                    System.out.println("✅ Reduced fee record for student: " + enrollment.getStudent().getEmail());
                }
            } catch (Exception e) {
                System.out.println("No fee record found to update for student: " + enrollment.getStudent().getEmail());
            }
        }

        enrollmentRepository.deleteById(id);
    }
}