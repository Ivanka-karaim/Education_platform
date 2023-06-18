package com.education_platform.data;

import com.education_platform.model.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long> {
    List<Course> findAll();

    Optional<Course> findById(Long id);

    <S extends Course> S save(S entity);

}
