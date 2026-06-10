package com.studentmgmt.controller;

import com.studentmgmt.dto.ApiResponse;
import com.studentmgmt.dto.StudentRequestDto;
import com.studentmgmt.dto.StudentResponseDto;
import com.studentmgmt.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    // ==========================================
    // POST - Create Student
    // ==========================================
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDto>> createStudent(
            @Valid @RequestBody StudentRequestDto requestDto) {

        log.info("POST /api/v1/students - Creating student: {}", requestDto.getEmail());
        StudentResponseDto created = studentService.createStudent(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student registered successfully.", created));
    }

    // ==========================================
    // GET - Fetch by ID
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentById(@PathVariable Long id) {
        log.info("GET /api/v1/students/{}", id);
        StudentResponseDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success("Student fetched successfully.", student));
    }

    // ==========================================
    // GET - Fetch by Roll Number
    // ==========================================
    @GetMapping("/roll/{rollNumber}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentByRollNumber(
            @PathVariable String rollNumber) {

        StudentResponseDto student = studentService.getStudentByRollNumber(rollNumber);
        return ResponseEntity.ok(ApiResponse.success("Student fetched successfully.", student));
    }

    // ==========================================
    // GET - Fetch by Email
    // ==========================================
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentByEmail(
            @PathVariable String email) {

        StudentResponseDto student = studentService.getStudentByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Student fetched successfully.", student));
    }

    // ==========================================
    // GET - All Students (Paginated)
    // ==========================================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<StudentResponseDto>>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<StudentResponseDto> students = studentService.getAllStudents(pageable);
        return ResponseEntity.ok(ApiResponse.success("Students fetched successfully.", students));
    }

    // ==========================================
    // GET - Search Students
    // ==========================================
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<StudentResponseDto>>> searchStudents(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<StudentResponseDto> result = studentService.searchStudents(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success("Search results fetched.", result));
    }

    // ==========================================
    // GET - Students by Department
    // ==========================================
    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getStudentsByDepartment(
            @PathVariable String department) {

        List<StudentResponseDto> students = studentService.getStudentsByDepartment(department);
        return ResponseEntity.ok(ApiResponse.success("Students fetched for department: " + department, students));
    }

    // ==========================================
    // PUT - Full Update
    // ==========================================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDto requestDto) {

        log.info("PUT /api/v1/students/{}", id);
        StudentResponseDto updated = studentService.updateStudent(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully.", updated));
    }

    // ==========================================
    // PATCH - Partial Update
    // ==========================================
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> partialUpdateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequestDto requestDto) {

        log.info("PATCH /api/v1/students/{}", id);
        StudentResponseDto updated = studentService.partialUpdateStudent(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully.", updated));
    }

    // ==========================================
    // DELETE - Hard Delete
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        log.info("DELETE /api/v1/students/{}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully."));
    }

    // ==========================================
    // PATCH - Deactivate (Soft Delete)
    // ==========================================
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateStudent(@PathVariable Long id) {
        studentService.deactivateStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deactivated successfully."));
    }

    // ==========================================
    // PATCH - Reactivate
    // ==========================================
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateStudent(@PathVariable Long id) {
        studentService.activateStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student activated successfully."));
    }

    // ==========================================
    // GET - All Departments
    // ==========================================
    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<String>>> getAllDepartments() {
        List<String> departments = studentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success("Departments fetched.", departments));
    }

    // ==========================================
    // GET - Count by Department
    // ==========================================
    @GetMapping("/departments/{department}/count")
    public ResponseEntity<ApiResponse<Long>> countStudentsByDepartment(@PathVariable String department) {
        Long count = studentService.countStudentsByDepartment(department);
        return ResponseEntity.ok(ApiResponse.success("Count for " + department, count));
    }

}
