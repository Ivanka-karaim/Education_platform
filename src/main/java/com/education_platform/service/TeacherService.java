package com.education_platform.service;

import com.education_platform.data.CourseRepository;
import com.education_platform.data.CourseTeacherRepository;
import com.education_platform.data.StudentTeacherRepository;
import com.education_platform.data.UserRepository;
import com.education_platform.model.CourseTeacher;
import com.education_platform.model.StudentTeacher;
import com.education_platform.model.UserCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeacherService {

    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentTeacherRepository studentTeacherRepository;

    public String checkClass(Long course_id,String teacher_id){
        List<CourseTeacher> courseTeacherList = courseTeacherRepository.findAllByCourseIdAndTeacherEmail(course_id, teacher_id);
        if (!courseTeacherList.isEmpty()){
            return courseTeacherList.get(0).getId();
        }
        return null;
    }

    public String generatedCode(){
        String code = "";
        do {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            code = uuid.substring(0, 6);
        }while (courseTeacherRepository.findById(code).isPresent());
        System.out.println(code);
        return code;
    }

    public String createClass(Long course_id, String teacher_id){
        CourseTeacher courseTeacher = new CourseTeacher(generatedCode(), userRepository.findByEmail(teacher_id).orElse(null), courseRepository.findById(course_id).orElse(null));
        courseTeacherRepository.save(courseTeacher);
        return courseTeacher.getId();
    }

    public String getCode(Long course_id,String teacher_id){
        String codeNow = checkClass(course_id, teacher_id);
        if (codeNow != null){
            return codeNow;
        }else{
            return createClass(course_id, teacher_id);
        }

    }
    public Long joinStudentInClass(String code, String user_id){
        CourseTeacher courseTeacher = courseTeacherRepository.findById(code).orElse(new CourseTeacher());
        UserCourse userCourse = courseService.joinCourse(user_id, courseTeacher.getCourse().getId());
        StudentTeacher studentTeacher = new StudentTeacher(userCourse, courseTeacher);
        studentTeacherRepository.save(studentTeacher);
        return courseTeacher.getCourse().getId();
    }
}
