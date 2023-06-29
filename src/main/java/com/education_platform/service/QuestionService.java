package com.education_platform.service;

import com.education_platform.data.QuestionRepository;
import com.education_platform.dto.AnswerDTO;
import com.education_platform.dto.QuestionDTO;
import com.education_platform.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class QuestionService {
    
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerService answerService;
    
    public List<QuestionDTO> getQuestionDTOsByTest(Long id){
        List<Question> questions = questionRepository.findAllByTestId(id);

        return parsingQuestionDTO(questions);
        
        
    }

    private List<QuestionDTO> parsingQuestionDTO(List<Question> list) {
        List<QuestionDTO> QuestionDTOs = new ArrayList<>();
        for (Question Question : list) {
            List<AnswerDTO> answers = answerService.getAnswerDTOsByQuestion(Question.getId());
            Collections.shuffle(answers);
            QuestionDTOs.add(QuestionDTO.builder()
                    .id(Question.getId())
                            .title(Question.getTitle())
                            .grade(Question.getGrade())
                            .type(Question.getType())
                            .answers(answers)
                    .build());
        }
        Collections.shuffle(QuestionDTOs);
        QuestionDTOs.sort(Comparator.comparing(QuestionDTO::getType));

        return QuestionDTOs;
    }
}
