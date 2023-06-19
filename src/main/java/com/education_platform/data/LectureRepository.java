package com.education_platform.data;

import com.education_platform.model.Lecture;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LectureRepository extends CrudRepository<Lecture, Long> {
    List<Lecture> findAllByModuleId(Long id);
}
