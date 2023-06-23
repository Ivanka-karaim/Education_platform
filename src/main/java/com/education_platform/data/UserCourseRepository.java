package com.education_platform.data;

import com.education_platform.model.Course;
import com.education_platform.model.User;
import com.education_platform.model.UserCourse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCourseRepository extends CrudRepository<UserCourse, Long> {

    Optional<UserCourse> findByUserEmailAndCourseId(String user_email, Long course_id);

    List<UserCourse> findAllByUserEmail(String user_email);


}
