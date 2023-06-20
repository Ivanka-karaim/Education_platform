package com.education_platform.service;

import com.education_platform.data.*;
import com.education_platform.dto.*;
import com.education_platform.model.*;
import com.education_platform.model.Module;
import jdk.incubator.vector.VectorOperators;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private TestService testService;

    public boolean userHasCourse(Long user_id, Long course_id) {
        UserCourse userCourse = userCourseRepository.findByUserIdAndCourseId(user_id, course_id).orElse(null);
        return userCourse != null;
    }



    public boolean enrollUserCourse(Long user_id, Long course_id){
        UserCourse userCourseDB = userCourseRepository.findByUserIdAndCourseId(user_id, course_id).orElse(null);
        if (userCourseDB != null){
            userCourseRepository.delete(userCourseDB);
            return false;
        }else {
            UserCourse userCourse = new UserCourse(userRepository.findById(user_id).orElse(new User()), courseRepository.findById(course_id).orElse(new Course()));
            userCourseRepository.save(userCourse);
            return true;
        }
    }
    public List<ShortCourseDTO> getAllCourses(){
        List<Course> courses = courseRepository.findAll();
        return parsingShortCoursesDTO(courses, Long.valueOf("0"));
    }

    public List<ShortCourseDTO> getAllCourses(Long user_id){
        List<Course> courses = courseRepository.findAll();
        return parsingShortCoursesDTO(courses, user_id);
    }

    public CourseDTO getCourseById(Long id){
        Course course = courseRepository.findById(id).get();
        List<ShortModuleDTO> moduleDTOS = moduleService.getModuleDTOsByCourse(course.getId());
        return parsingCourseDTO(course, moduleDTOS,Long.valueOf("0"));
    }
    public CourseDTO getCourseById(Long id, Long user_id){
        Course course = courseRepository.findById(id).get();
        List<ShortModuleDTO> moduleDTOS = moduleService.getModuleDTOsByCourse(course.getId());
        return parsingCourseDTO(course, moduleDTOS,user_id);
    }



    private CourseDTO parsingCourseDTO(Course course, List<ShortModuleDTO> moduleDTOS, Long user_id) {
        return CourseDTO.builder()
                            .id(course.getId())
                            .category(course.getCategory().getTitle())
                            .description(course.getDescription())
                            .name(course.getName())
                            .teacher(course.getTeacher())
                            .modules(moduleDTOS)
                .user_enroll(userCourseRepository.findByUserIdAndCourseId(user_id, course.getId()).orElse(null) != null)
                    .build();

    }
    private List<ShortCourseDTO> parsingShortCoursesDTO(List<Course> list, Long user_id) {
        List<ShortCourseDTO> CourseDTOs = new ArrayList<>();

        for (Course course : list) {
            CourseDTOs.add(ShortCourseDTO.builder()
                    .id(course.getId())
                    .category(course.getCategory().getTitle())
                    .description(course.getDescription())
                    .name(course.getName())
                    .teacher(course.getTeacher())
                    .user_enroll(userCourseRepository.findByUserIdAndCourseId(user_id, course.getId()).orElse(null) != null)
                    .build());
        }

        return CourseDTOs;
    }
}

