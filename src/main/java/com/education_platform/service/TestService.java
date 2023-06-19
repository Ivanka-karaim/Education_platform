package com.education_platform.service;

import com.education_platform.data.TestRepository;
import com.education_platform.dto.LectureDTO;
import com.education_platform.dto.ModuleDTO;
import com.education_platform.dto.ShortTestDTO;
import com.education_platform.dto.TestDTO;
import com.education_platform.model.Module;
import com.education_platform.model.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    TestRepository testRepository;

    @Autowired
    QuestionService questionService;

    public ShortTestDTO getTestDTOByModule(Long id) {
        Test test = testRepository.findByModuleId(id).orElse(new Test());
        return parsingShortTestDTO(test);

    }

    public TestDTO getTestDTOById(Long id) {
        Test test = testRepository.findById(id).orElse(new Test());
        return parsingTestDTO(test);
    }

    private ShortTestDTO parsingShortTestDTO(Test test) {
        return ShortTestDTO.builder()
                .id(test.getId())
                .duration(test.getDuration())
                .maxGrade(test.getMaxGrade())
                .title(test.getTitle())
                .build();

    }

    private TestDTO parsingTestDTO(Test test) {
        return TestDTO.builder()
                .id(test.getId())
                .duration(test.getDuration())
                .maxGrade(test.getMaxGrade())
                .title(test.getTitle())
                .questions(questionService.getQuestionDTOsByTest(test.getId()))
                .build();

    }
}
