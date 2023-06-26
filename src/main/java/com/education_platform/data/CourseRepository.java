package com.education_platform.data;

import com.education_platform.model.Course;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long> {
    List<Course> findAll(Pageable pageable);

    List<Course> findAllByCategoryId(Long category_id, Pageable pageable);
    List<Course> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);
    <S extends Course> S save(S entity);

}
