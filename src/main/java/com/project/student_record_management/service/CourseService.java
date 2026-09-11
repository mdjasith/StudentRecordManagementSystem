package com.project.student_record_management.service;

import com.project.student_record_management.entity.Course;
import com.project.student_record_management.exception.ResourceNotFoundException;
import com.project.student_record_management.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course createCourse(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new RuntimeException("Course with code " + course.getCourseCode() + " already exists!");
        }
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Course existingCourse = getCourseById(id);
        existingCourse.setCourseName(courseDetails.getCourseName());
        existingCourse.setCredits(courseDetails.getCredits());
        existingCourse.setProfessorName(courseDetails.getProfessorName());
        return courseRepository.save(existingCourse);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}