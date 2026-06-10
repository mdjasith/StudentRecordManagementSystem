package com.studentmgmt.service;

import com.studentmgmt.dto.StudentRequestDto;
import com.studentmgmt.dto.StudentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    StudentResponseDto createStudent(StudentRequestDto requestDto);

    StudentResponseDto getStudentById(Long id);

    StudentResponseDto getStudentByRollNumber(String rollNumber);

    StudentResponseDto getStudentByEmail(String email);

    Page<StudentResponseDto> getAllStudents(Pageable pageable);

    Page<StudentResponseDto> searchStudents(String keyword, Pageable pageable);

    List<StudentResponseDto> getStudentsByDepartment(String department);

    StudentResponseDto updateStudent(Long id, StudentRequestDto requestDto);

    StudentResponseDto partialUpdateStudent(Long id, StudentRequestDto requestDto);

    void deleteStudent(Long id);

    void deactivateStudent(Long id);

    void activateStudent(Long id);

    List<String> getAllDepartments();

    Long countStudentsByDepartment(String department);

}
