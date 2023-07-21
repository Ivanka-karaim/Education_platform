package com.education_platform.data;

import com.education_platform.model.Course;
import com.education_platform.model.CourseTeacher;
import io.micrometer.observation.Observation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CourseTeacherRepository extends CrudRepository<CourseTeacher, String> {
    List<CourseTeacher> findAllByCourseIdAndTeacherEmail(Long course_id,String teacher_email);
}
