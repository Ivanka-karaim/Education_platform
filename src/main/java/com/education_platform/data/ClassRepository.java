package com.education_platform.data;

import com.education_platform.model.Class;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ClassRepository extends CrudRepository<Class, String> {
    List<Class> findAllByCourseIdAndTeacherEmail(Long course_id, String teacher_email);
}
