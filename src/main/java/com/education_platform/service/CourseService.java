package com.education_platform.service;

import com.education_platform.data.CourseRepository;
import com.education_platform.dto.CourseDTO;
import com.education_platform.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<CourseDTO> getAllCourses(){
        List<Course> courses = courseRepository.findAll();
        return parsingCoursesDTO(courses);
    }

    private List<CourseDTO> parsingCoursesDTO(List<Course> list) {
        List<CourseDTO> CourseDTOs = new ArrayList<>();

        for (Course course : list) {
            CourseDTOs.add(CourseDTO.builder()
                            .id(course.getId())
                            .category(course.getCategory().getTitle())
                            .description(course.getDescription())
                            .name(course.getName())
                            .teacher(course.getTeacher())
                    .build());
        }

        return CourseDTOs;
    }
}

