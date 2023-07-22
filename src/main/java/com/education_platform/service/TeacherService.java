package com.education_platform.service;

import com.education_platform.data.CourseRepository;
import com.education_platform.data.ClassRepository;
import com.education_platform.data.StudentClassRepository;
import com.education_platform.data.UserRepository;
import com.education_platform.model.Class;
import com.education_platform.model.StudentClass;
import com.education_platform.model.UserCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeacherService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentClassRepository studentClassRepository;

    public String checkClass(Long course_id,String teacher_id){
        List<Class> classList = classRepository.findAllByCourseIdAndTeacherEmail(course_id, teacher_id);
        if (!classList.isEmpty()){
            return classList.get(0).getId();
        }
        return null;
    }

    public String generatedCode(){
        String code = "";
        do {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            code = uuid.substring(0, 6);
        }while (classRepository.findById(code).isPresent());
        System.out.println(code);
        return code;
    }

    public String createClass(Long course_id, String teacher_id){
        Class courseTeacher = new Class(generatedCode(), userRepository.findByEmail(teacher_id).orElse(null), courseRepository.findById(course_id).orElse(null));
        classRepository.save(courseTeacher);
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
        Class aClass = classRepository.findById(code).orElse(new Class());
        UserCourse userCourse = courseService.joinCourse(user_id, aClass.getCourse().getId());
        StudentClass studentClass = new StudentClass(userCourse, aClass);
        studentClassRepository.save(studentClass);
        return aClass.getCourse().getId();
    }
}
