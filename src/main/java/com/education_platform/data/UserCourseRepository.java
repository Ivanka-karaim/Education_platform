package com.education_platform.data;

import com.education_platform.model.UserCourse;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserCourseRepository extends CrudRepository<UserCourse, Long> {

    Optional<UserCourse> findByUserIdAndCourseId(Long user_id, Long course_id);


}
