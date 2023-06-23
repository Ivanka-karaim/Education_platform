package com.education_platform.data;

import com.education_platform.model.CourseComment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseCommentRepository extends CrudRepository<CourseComment, Long> {

    List<CourseComment> findAllByCourseId(Long course_id);

}
