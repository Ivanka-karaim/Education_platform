package com.education_platform.data;

import com.education_platform.model.Course;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long> {
    List<Course> findAll();

    List<Course> findAllByCategoryId(Long category_id);
    List<Course> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
    <S extends Course> S save(S entity);

}
