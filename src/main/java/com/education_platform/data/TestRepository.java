package com.education_platform.data;

import com.education_platform.model.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends CrudRepository<Test, Long> {
    Optional<Test> findByModuleId(Long id);

    List<Test> findAllByModule_CourseId(Long courseId);


}
