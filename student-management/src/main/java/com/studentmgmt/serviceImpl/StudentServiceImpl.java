package com.studentmgmt.serviceImpl;

import com.studentmgmt.dto.StudentRequestDto;
import com.studentmgmt.dto.StudentResponseDto;
import com.studentmgmt.exception.DuplicateResourceException;
import com.studentmgmt.exception.StudentNotFoundException;
import com.studentmgmt.model.Student;
import com.studentmgmt.repository.StudentRepository;
import com.studentmgmt.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto requestDto) {
        log.debug("Creating student with email: {}", requestDto.getEmail());

        if (studentRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("A student with email '" + requestDto.getEmail() + "' already exists.");
        }
        if (studentRepository.existsByRollNumber(requestDto.getRollNumber())) {
            throw new DuplicateResourceException("Roll number '" + requestDto.getRollNumber() + "' is already taken.");
        }

        Student student = mapToEntity(requestDto);
        Student saved = studentRepository.save(student);
        log.info("Student created successfully with id: {}", saved.getId());
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return mapToResponseDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentByRollNumber(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with roll number: " + rollNumber));
        return mapToResponseDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with email: " + email));
        return mapToResponseDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponseDto> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponseDto> searchStudents(String keyword, Pageable pageable) {
        return studentRepository.searchStudents(keyword, pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDto> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartment(department)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto requestDto) {
        log.debug("Updating student with id: {}", id);

        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        if (studentRepository.existsByEmailAndIdNot(requestDto.getEmail(), id)) {
            throw new DuplicateResourceException("Email '" + requestDto.getEmail() + "' is already used by another student.");
        }
        if (studentRepository.existsByRollNumberAndIdNot(requestDto.getRollNumber(), id)) {
            throw new DuplicateResourceException("Roll number '" + requestDto.getRollNumber() + "' is already used by another student.");
        }

        updateEntityFromDto(existing, requestDto);
        Student updated = studentRepository.save(existing);
        log.info("Student updated successfully with id: {}", id);
        return mapToResponseDto(updated);
    }

    @Override
    @Transactional
    public StudentResponseDto partialUpdateStudent(Long id, StudentRequestDto requestDto) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        if (requestDto.getFirstName() != null) existing.setFirstName(requestDto.getFirstName());
        if (requestDto.getLastName() != null) existing.setLastName(requestDto.getLastName());
        if (requestDto.getPhone() != null) existing.setPhone(requestDto.getPhone());
        if (requestDto.getDateOfBirth() != null) existing.setDateOfBirth(requestDto.getDateOfBirth());
        if (requestDto.getGender() != null) existing.setGender(requestDto.getGender());
        if (requestDto.getAddress() != null) existing.setAddress(requestDto.getAddress());
        if (requestDto.getDepartment() != null) existing.setDepartment(requestDto.getDepartment());
        if (requestDto.getCourse() != null) existing.setCourse(requestDto.getCourse());
        if (requestDto.getYearOfStudy() != null) existing.setYearOfStudy(requestDto.getYearOfStudy());
        if (requestDto.getCgpa() != null) existing.setCgpa(requestDto.getCgpa());

        if (requestDto.getEmail() != null && !requestDto.getEmail().equals(existing.getEmail())) {
            if (studentRepository.existsByEmailAndIdNot(requestDto.getEmail(), id)) {
                throw new DuplicateResourceException("Email '" + requestDto.getEmail() + "' is already taken.");
            }
            existing.setEmail(requestDto.getEmail());
        }

        if (requestDto.getRollNumber() != null && !requestDto.getRollNumber().equals(existing.getRollNumber())) {
            if (studentRepository.existsByRollNumberAndIdNot(requestDto.getRollNumber(), id)) {
                throw new DuplicateResourceException("Roll number '" + requestDto.getRollNumber() + "' is already taken.");
            }
            existing.setRollNumber(requestDto.getRollNumber());
        }

        return mapToResponseDto(studentRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
        log.info("Student deleted with id: {}", id);
    }

    @Override
    @Transactional
    public void deactivateStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        student.setIsActive(false);
        studentRepository.save(student);
        log.info("Student deactivated with id: {}", id);
    }

    @Override
    @Transactional
    public void activateStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        student.setIsActive(true);
        studentRepository.save(student);
        log.info("Student activated with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllDepartments() {
        return studentRepository.findAllDepartments();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countStudentsByDepartment(String department) {
        return studentRepository.countByDepartment(department);
    }

    // ============================================================
    // Private Helper Methods
    // ============================================================

    private Student mapToEntity(StudentRequestDto dto) {
        return Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .address(dto.getAddress())
                .department(dto.getDepartment())
                .course(dto.getCourse())
                .yearOfStudy(dto.getYearOfStudy())
                .rollNumber(dto.getRollNumber())
                .cgpa(dto.getCgpa())
                .isActive(true)
                .build();
    }

    private void updateEntityFromDto(Student student, StudentRequestDto dto) {
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setGender(dto.getGender());
        student.setAddress(dto.getAddress());
        student.setDepartment(dto.getDepartment());
        student.setCourse(dto.getCourse());
        student.setYearOfStudy(dto.getYearOfStudy());
        student.setRollNumber(dto.getRollNumber());
        student.setCgpa(dto.getCgpa());
    }

    private StudentResponseDto mapToResponseDto(Student student) {
        return StudentResponseDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .fullName(student.getFirstName() + " " + student.getLastName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .address(student.getAddress())
                .department(student.getDepartment())
                .course(student.getCourse())
                .yearOfStudy(student.getYearOfStudy())
                .rollNumber(student.getRollNumber())
                .cgpa(student.getCgpa())
                .isActive(student.getIsActive())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }

}
