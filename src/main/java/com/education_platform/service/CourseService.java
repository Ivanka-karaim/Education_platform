package com.education_platform.service;

import com.education_platform.data.*;
import com.education_platform.dto.*;
import com.education_platform.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final int COUNT_COURSE=20;

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

    @Autowired
    private CourseCommentRepository courseCommentRepository;

    public int getCountPage(){
        return (int) Math.ceil((float) courseRepository.count()/COUNT_COURSE);
    }

    public boolean userHasCourse(String user_id, Long course_id) {
        UserCourse userCourse = userCourseRepository.findByUserEmailAndCourseId(user_id, course_id).orElse(null);
        return userCourse != null;
    }






    public List<ShortCourseDTO> getAllCoursesByCategory(Long category_id, int page) {
        Pageable pageable = PageRequest.of((page-1)*COUNT_COURSE, COUNT_COURSE, Sort.by("id"));
        List<Course> courses = courseRepository.findAllByCategoryId(category_id, pageable);
        return parsingShortCoursesDTO(courses);
    }

    public List<CourseComment> getAllCourseComment(Long course_id) {
        return courseCommentRepository.findAllByCourseId(course_id);
    }

    public float getRatingCourse(List<CourseComment> courseComments) {
        int count = 0;
        float sum = 0;
        for (CourseComment courseComment : courseComments) {
            sum += courseComment.getRating();
            count += 1;
        }

        return count == 0 ? 0 : sum / count;
    }

    public List<ShortCourseDTO> getAllCoursesSearch(String searchQuery, int page) {
        Pageable pageable = PageRequest.of((page-1)*COUNT_COURSE, COUNT_COURSE, Sort.by("id"));
        List<Course> courses = courseRepository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchQuery, searchQuery, pageable);
        return parsingShortCoursesDTO(courses);
    }

    public boolean enrollUserCourse(String user_id, Long course_id) {
        UserCourse userCourseDB = userCourseRepository.findByUserEmailAndCourseId(user_id, course_id).orElse(null);
        if (userCourseDB != null) {
            userCourseRepository.delete(userCourseDB);
            return false;
        } else {
            UserCourse userCourse = new UserCourse(userRepository.findByEmail(user_id).orElse(new User()), courseRepository.findById(course_id).orElse(new Course()));
            userCourseRepository.save(userCourse);
            return true;
        }
    }

    public UserCourse joinCourse(String user_id, Long course_id){
        UserCourse userCourseDB = userCourseRepository.findByUserEmailAndCourseId(user_id, course_id).orElse(null);
        if (userCourseDB != null) {
            return userCourseDB;
        } else {
            UserCourse userCourse = new UserCourse(userRepository.findByEmail(user_id).orElse(new User()), courseRepository.findById(course_id).orElse(new Course()));
            userCourseRepository.save(userCourse);
            return userCourse;
        }
    }

    public boolean checkForCourseId(String numbers, Long course_id) {
        String[] numberArray = numbers.split(";");
        for (String number : numberArray) {
            if (number.equals(course_id.toString())) {
                return true;
            }
        }
        return false;
    }

    public boolean likeCourse(HttpServletResponse response, HttpServletRequest request, Long course_id)  {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("likeCourses")) {
                    String courses = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    if (checkForCourseId(courses, course_id)){
                        String[] numberArray = courses.split(";");
                        if (numberArray.length == 1){
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        }else {
                            StringBuilder updatedCourse = new StringBuilder();
                            for (String c : numberArray) {
                                if (!c.equals(course_id.toString())) { // Видаляємо елемент зі списку (3 у вашому випадку)
                                    updatedCourse.append(c).append(";");
                                }
                            }
                            if (updatedCourse.length() > 0) {
                                updatedCourse.deleteCharAt(updatedCourse.length() - 1); // Видаляємо останній зайвий кома
                            }
                            courses = updatedCourse.toString();
                            System.out.println(courses);
                        }
                    }else {
                        courses += ";" + course_id.toString();
                        System.out.println(courses);
                    }

                    cookie.setValue(URLEncoder.encode(courses, StandardCharsets.UTF_8));
                    response.addCookie(cookie);
                    return true;
                }
            }
        }
        Cookie cookie = new Cookie("likeCourses", course_id.toString());
        cookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(cookie);
        return true;
    }

    public List<ShortCourseDTO> getFavouriteCourses(HttpServletRequest request){
        List<Course> courses = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("likeCourses")) {
                    String coursesStr = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    String[] numberArray = coursesStr.split(";");
                    for( String number: numberArray){
                        courses.add(courseRepository.findById(Long.valueOf(number)).orElse(null));
                    }
                }
            }
        }
        return parsingShortCoursesDTO(courses);
    }

    public List<Long> getListCoursesCookie(HttpServletRequest request){
        String [] numberArray = new String[0];
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("likeCourses")) {
                    numberArray = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8).split(";");
                }
            }
        }
        System.out.println(numberArray);
        System.out.println(11111111);
        return  Arrays.stream(numberArray)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public List<ShortCourseDTO> getAllCourses(int page) {

        Pageable pageable = PageRequest.of((page-1)*COUNT_COURSE, COUNT_COURSE, Sort.by("id"));

        List<Course> courses = courseRepository.findAll(pageable);
        return parsingShortCoursesDTO(courses);
    }


    public List<ShortCourseDTO> getAllCoursesByUser(String user_id) {
        List<UserCourse> userCourses = userCourseRepository.findAllByUserEmail(user_id);
        List<Course> courses = new ArrayList<>();
        for (UserCourse userCourse : userCourses) {
            courses.add((userCourse.getCourse()));
        }
        return parsingShortCoursesDTO(courses);
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id).get();
        List<ShortModuleDTO> moduleDTOS = moduleService.getModuleDTOsByCourse(course.getId());
        return parsingCourseDTO(course, moduleDTOS, "0");
    }



    public CourseDTO getCourseById(Long id, String user_id) {
        Course course = courseRepository.findById(id).get();
        List<ShortModuleDTO> moduleDTOS = moduleService.getModuleDTOsByCourse(course.getId());
        return parsingCourseDTO(course, moduleDTOS, user_id);
    }

    public boolean addComment(int rating, String comment, Long course_id, String user_id) {
        CourseComment courseComment = new CourseComment(rating, comment, courseRepository.findById(course_id).orElse(null), userRepository.findByEmail(user_id).orElse(null));
        courseCommentRepository.save(courseComment);
        return true;
    }

    public boolean delComment(Long comment_id, String user_id) {
        CourseComment courseComment = courseCommentRepository.findById(comment_id).orElse(new CourseComment());
        if (!Objects.equals(courseComment.getUser().getEmail(), user_id)) {
            return false;
        }
        courseCommentRepository.delete(courseComment);
        return true;

    }




    private CourseDTO parsingCourseDTO(Course course, List<ShortModuleDTO> moduleDTOS, String user_id) {
        System.out.println(course);
        UserCourse userCourse = userCourseRepository.findByUserEmailAndCourseId(user_id, course.getId()).orElse(null);
        System.out.println(userCourse);
        return CourseDTO.builder()
                .id(course.getId())
                .category(course.getCategory().getTitle())
                .description(course.getDescription())
                .name(course.getName())
                .teacher(course.getTeacher())
                .modules(moduleDTOS)
                .user_enroll(userCourse==null?null:userCourse.getUser())
                .certified(userCourse==null?false:userCourse.isCertificate())
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

