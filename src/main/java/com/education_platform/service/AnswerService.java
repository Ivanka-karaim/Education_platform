package com.education_platform.service;

import com.education_platform.data.AnswerRepository;
import com.education_platform.dto.AnswerDTO;
import com.education_platform.dto.AnswerDTO;
import com.education_platform.model.Answer;
import com.education_platform.model.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService {
    
    @Autowired
    AnswerRepository answerRepository;
    
    public List<AnswerDTO> getAnswerDTOsByQuestion(Long id){
        List<Answer> answers = answerRepository.findAllByQuestionId(id);
        return parsingAnswerDTO(answers);
        
    }

    private List<AnswerDTO> parsingAnswerDTO(List<Answer> list) {
        List<AnswerDTO> AnswerDTOs = new ArrayList<>();

        for (Answer Answer : list) {
            AnswerDTOs.add(AnswerDTO.builder()
                    .id(Answer.getId())
                    .title(Answer.getTitle())
                            .correctness(Answer.isCorrectness())
                    .build());
        }

        return AnswerDTOs;
    }
}
