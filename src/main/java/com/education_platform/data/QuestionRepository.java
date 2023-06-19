package com.education_platform.data;

import com.education_platform.model.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findAllByTestId(Long id);
}
