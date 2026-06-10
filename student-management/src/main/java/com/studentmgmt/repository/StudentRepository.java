package com.studentmgmt.repository;

import com.studentmgmt.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    Optional<Student> findByRollNumber(String rollNumber);

    boolean existsByEmail(String email);

    boolean existsByRollNumber(String rollNumber);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByRollNumberAndIdNot(String rollNumber, Long id);

    List<Student> findByDepartment(String department);

    List<Student> findByCourse(String course);

    List<Student> findByIsActive(Boolean isActive);

    @Query("SELECT s FROM Student s WHERE " +
           "(:keyword IS NULL OR " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.department) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Student> searchStudents(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT s.department FROM Student s ORDER BY s.department")
    List<String> findAllDepartments();

    @Query("SELECT COUNT(s) FROM Student s WHERE s.department = :department")
    Long countByDepartment(@Param("department") String department);

}
