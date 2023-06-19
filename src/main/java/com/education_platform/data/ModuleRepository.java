package com.education_platform.data;

import org.springframework.data.repository.CrudRepository;
import com.education_platform.model.Module;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends CrudRepository<Module, Long> {
    List<Module> findAllByCourseId(Long id);
    Optional<Module> findById(Long id);

    Optional<Module> findByCourseIdAndNumber(Long id, int number);

}
