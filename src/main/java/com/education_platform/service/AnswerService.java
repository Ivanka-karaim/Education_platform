package com.education_platform.service;

import com.education_platform.data.*;
import com.education_platform.dto.AnswerDTO;
import com.education_platform.dto.AnswerDTO;
import com.education_platform.model.*;
import com.education_platform.model.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AnswerService {
    
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserTestRepository userTestRepository;
    
    public List<AnswerDTO> getAnswerDTOsByQuestion(Long id){
        List<Answer> answers = answerRepository.findAllByQuestionId(id);
        return parsingAnswerDTO(answers);
        
    }

    public float resultTest(Map<String, String> formValues, Long user_id){
        formValues.remove("_csrf");
        System.out.println(formValues);
        float grade = 0;
        User user = userRepository.findById(user_id).orElse(new User());
        Answer answer = new Answer();
        for(String value: formValues.values()) {
            answer = answerRepository.findById(Long.valueOf(value)).orElse(new Answer());
            UserAnswer userAnswer = new UserAnswer("", answer, user);
            userAnswerRepository.save(userAnswer);
            if (answer.isCorrectness()) {
                grade += answer.getQuestion().getGrade();
            }
        }
        UserTest userTest = new UserTest(grade, user, testRepository.findById(answer.getQuestion().getTest().getId()).orElse(new Test()));
        userTestRepository.save(userTest);
        return grade;
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
