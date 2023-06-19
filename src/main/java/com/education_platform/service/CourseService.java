package com.education_platform.service;

import com.education_platform.data.CourseRepository;
import com.education_platform.data.ModuleRepository;
import com.education_platform.dto.CourseDTO;
import com.education_platform.dto.ModuleDTO;
import com.education_platform.dto.ShortCourseDTO;
import com.education_platform.dto.ShortModuleDTO;
import com.education_platform.model.Course;
import com.education_platform.model.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleService moduleService;

    public List<ShortCourseDTO> getAllCourses(){
        List<Course> courses = courseRepository.findAll();
        return parsingShortCoursesDTO(courses);
    }

    public CourseDTO getCourseById(Long id){
        System.out.println(11);
        System.out.println(id);
        Course course = courseRepository.findById(id).get();
        System.out.println(22);
        List<ShortModuleDTO> moduleDTOS = moduleService.getModuleDTOsByCourse(course.getId());
        return parsingCourseDTO(course, moduleDTOS);
    }

    private CourseDTO parsingCourseDTO(Course course, List<ShortModuleDTO> moduleDTOS) {
        return CourseDTO.builder()
                            .id(course.getId())
                            .category(course.getCategory().getTitle())
                            .description(course.getDescription())
                            .name(course.getName())
                            .teacher(course.getTeacher())
                            .modules(moduleDTOS)
                    .build();

    }
    private List<ShortCourseDTO> parsingShortCoursesDTO(List<Course> list) {
        List<ShortCourseDTO> CourseDTOs = new ArrayList<>();

        for (Course course : list) {
            CourseDTOs.add(ShortCourseDTO.builder()
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

