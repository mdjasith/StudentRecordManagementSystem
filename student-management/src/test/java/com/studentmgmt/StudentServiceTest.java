package com.studentmgmt;

import com.studentmgmt.dto.StudentRequestDto;
import com.studentmgmt.dto.StudentResponseDto;
import com.studentmgmt.exception.DuplicateResourceException;
import com.studentmgmt.exception.StudentNotFoundException;
import com.studentmgmt.model.Student;
import com.studentmgmt.repository.StudentRepository;
import com.studentmgmt.serviceImpl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private StudentRequestDto requestDto;
    private Student student;

    @BeforeEach
    void setUp() {
        requestDto = StudentRequestDto.builder()
                .firstName("Ravi")
                .lastName("Kumar")
                .email("ravi.kumar@example.com")
                .phone("9876543210")
                .dateOfBirth(LocalDate.of(2002, 5, 15))
                .gender("Male")
                .department("Computer Science")
                .course("B.Tech")
                .yearOfStudy(2)
                .rollNumber("CS2024001")
                .cgpa(8.5)
                .build();

        student = Student.builder()
                .id(1L)
                .firstName("Ravi")
                .lastName("Kumar")
                .email("ravi.kumar@example.com")
                .phone("9876543210")
                .department("Computer Science")
                .course("B.Tech")
                .yearOfStudy(2)
                .rollNumber("CS2024001")
                .cgpa(8.5)
                .isActive(true)
                .build();
    }

    @Test
    void shouldCreateStudentSuccessfully() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.existsByRollNumber(anyString())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponseDto result = studentService.createStudent(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("ravi.kumar@example.com");
        assertThat(result.getRollNumber()).isEqualTo("CS2024001");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> studentService.createStudent(requestDto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void shouldFetchStudentByIdSuccessfully() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponseDto result = studentService.getStudentById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFullName()).isEqualTo("Ravi Kumar");
    }

    @Test
    void shouldThrowExceptionWhenStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void shouldDeleteStudentSuccessfully() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        assertThatNoException().isThrownBy(() -> studentService.deleteStudent(1L));
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentStudent() {
        when(studentRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> studentService.deleteStudent(99L))
                .isInstanceOf(StudentNotFoundException.class);
    }

}
