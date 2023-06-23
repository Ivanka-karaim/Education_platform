package com.education_platform.service;

import com.education_platform.data.*;
import com.education_platform.dto.*;
import com.education_platform.model.*;
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

    public boolean userHasCourse(String user_id, Long course_id) {
        UserCourse userCourse = userCourseRepository.findByUserEmailAndCourseId(user_id, course_id).orElse(null);
        return userCourse != null;
    }

    public float getRatingByCourseAndUser(Long course_id, String user_id){
        List<Test> tests = testService.getAllTestByCourse(course_id);
        List<UserTest> userTests = testService.getAllUserTestByCourseAndUser(user_id, course_id);
        return tests.size()==0?1:(float) userTests.size()/tests.size();
    }

    public List<ShortCourseDTO> getAllCoursesByCategory(Long category_id){
        List<Course> courses = courseRepository.findAllByCategoryId(category_id);
        return parsingShortCoursesDTO(courses, "0");
    }

    public List<ShortCourseDTO> getAllCoursesByCategory(Long category_id, String user_id){
        List<Course> courses = courseRepository.findAllByCategoryId(category_id);
        return parsingShortCoursesDTO(courses, user_id);
    }
    public List<ShortCourseDTO> getAllCoursesSearch(String searchQuery, String user_id){
        List<Course> courses = courseRepository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchQuery, searchQuery);
        return parsingShortCoursesDTO(courses, user_id);
    }
    public List<ShortCourseDTO> getAllCoursesSearch(String searchQuery){
        List<Course> courses = courseRepository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchQuery, searchQuery);
        return parsingShortCoursesDTO(courses,"0");
    }

    public boolean enrollUserCourse(String user_id, Long course_id){
        UserCourse userCourseDB = userCourseRepository.findByUserEmailAndCourseId(user_id, course_id).orElse(null);
        if (userCourseDB != null){
            userCourseRepository.delete(userCourseDB);
            return false;
        }else {
            UserCourse userCourse = new UserCourse(userRepository.findByEmail(user_id).orElse(new User()), courseRepository.findById(course_id).orElse(new Course()));
            userCourseRepository.save(userCourse);
            return true;
        }
    }
    public List<ShortCourseDTO> getAllCourses(){
        List<Course> courses = courseRepository.findAll();
        return parsingShortCoursesDTO(courses, "0");
    }

    public List<ShortCourseDTO> getAllCourses(String user_id){
        List<Course> courses = courseRepository.findAll();
        return parsingShortCoursesDTO(courses, user_id);
    }

    public List<ShortCourseDTO> getAllCoursesByUser(String user_id){
        List<UserCourse> userCourses = userCourseRepository.findAllByUserEmail(user_id);
        List<Course> courses = new ArrayList<>();
        for(UserCourse userCourse: userCourses){
            courses.add((userCourse.getCourse()));
        }
        return parsingShortCoursesDTO(courses, user_id);
    }

    public CourseDTO getCourseById(Long id){
        Course course = courseRepository.findById(id).get();
        List<ShortModuleDTO> moduleDTOS = moduleService.getModuleDTOsByCourse(course.getId());
        return parsingCourseDTO(course, moduleDTOS,"0");
    }
    public CourseDTO getCourseById(Long id, String user_id){
        Course course = courseRepository.findById(id).get();
        List<ShortModuleDTO> moduleDTOS = moduleService.getModuleDTOsByCourse(course.getId());
        return parsingCourseDTO(course, moduleDTOS,user_id);
    }



    private CourseDTO parsingCourseDTO(Course course, List<ShortModuleDTO> moduleDTOS, String user_id) {
        return CourseDTO.builder()
                            .id(course.getId())
                            .category(course.getCategory().getTitle())
                            .description(course.getDescription())
                            .name(course.getName())
                            .teacher(course.getTeacher())
                            .modules(moduleDTOS)
                .user_enroll(userCourseRepository.findByUserEmailAndCourseId(user_id, course.getId()).orElse(null) != null)
                    .build();

    }
    private List<ShortCourseDTO> parsingShortCoursesDTO(List<Course> list, String user_id) {
        List<ShortCourseDTO> CourseDTOs = new ArrayList<>();

        for (Course course : list) {
            CourseDTOs.add(ShortCourseDTO.builder()
                    .id(course.getId())
                    .category(course.getCategory().getTitle())
                    .description(course.getDescription())
                    .name(course.getName())
                    .teacher(course.getTeacher())
                    .user_enroll(userCourseRepository.findByUserEmailAndCourseId(user_id, course.getId()).orElse(null) != null)
                    .build());
        }

        return CourseDTOs;
    }
}

