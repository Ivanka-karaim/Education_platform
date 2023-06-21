package com.education_platform.service;

import com.education_platform.data.*;
import com.education_platform.dto.ShortTestDTO;
import com.education_platform.dto.TestDTO;
import com.education_platform.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestService {

    @Autowired
    TestRepository testRepository;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserTestRepository userTestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserAnswerRepository userAnswerRepository;

    public ShortTestDTO getTestDTOByModule(Long id) {
        Test test = testRepository.findByModuleId(id).orElse(new Test());
        return parsingShortTestDTO(test);

    }

    public List<Test> getAllTestByCourse(Long course_id){
        return testRepository.findAllByModule_CourseId(course_id);
    }

    public  List<UserTest> getAllUserTestByCourseAndUser(Long user_id, Long course_id){
        return userTestRepository.findAllByUserIdAndTest_Module_CourseId(user_id, course_id);
    }

    public UserTest userFinishedTest(Long user_id, Long test_id) {
        return userTestRepository.findByUserIdAndTestId(user_id, test_id).orElse(null);

    }


    public TestDTO getTestDTOById(Long id) {
        Test test = testRepository.findById(id).orElse(new Test());
        return parsingTestDTO(test);
    }

    public float resultTest(Map<String, String> formValues, Long user_id) {
        formValues.remove("_csrf");
        Long question = Long.valueOf("0");
        float grade = 0;
        User user = userRepository.findById(user_id).orElse(new User());
        Answer answer = new Answer();
        for (Map.Entry<String, String> entry : formValues.entrySet()) {
            if (entry.getKey().contains("one_")) {
                System.out.println("one");
                answer = answerRepository.findById(Long.valueOf(entry.getValue())).orElse(new Answer());
                if (answer.isCorrectness()) {
                    grade += answer.getQuestion().getGrade();
                }
                UserAnswer userAnswer = new UserAnswer("", answer, user);
                userAnswerRepository.save(userAnswer);
            } else if (entry.getKey().contains("few_") && !entry.getKey().contains("few_"+question)) {
                float countCorrectFew = 0;
                Map<String, String> fewAnswer = getMapForQuestion(formValues, entry.getKey().split("_")[0] + "_" + entry.getKey().split("_")[1]);
                System.out.println(fewAnswer);
                for (Map.Entry<String, String> few : fewAnswer.entrySet()) {
                    answer = answerRepository.findById(Long.valueOf(few.getValue())).orElse(new Answer());
                    UserAnswer userAnswer = new UserAnswer("", answer, user);
                    userAnswerRepository.save(userAnswer);
                    if (answer.isCorrectness()) {
                        countCorrectFew += 1;
                    }
                }
                question = answer.getQuestion().getId();
                List<Answer> answers = answerRepository.findAllByQuestionId(answer.getQuestion().getId()).stream()
                        .filter(Answer::isCorrectness)
                        .toList();
                grade += (countCorrectFew / answers.size())*answer.getQuestion().getGrade();

            } else if (entry.getKey().contains("write_")) {
                answer = answerRepository.findById(Long.valueOf(entry.getKey().replace("write_", ""))).orElse(new Answer());
                UserAnswer userAnswer = new UserAnswer(entry.getValue(), answer, user);
                userAnswerRepository.save(userAnswer);
                if (answer.getTitle().equals(entry.getValue())) {
                    grade += answer.getQuestion().getGrade();
                }
            }
        }
        UserTest userTest = new UserTest(grade, user, testRepository.findById(answer.getQuestion().getTest().getId()).orElse(new Test()));
        userTestRepository.save(userTest);
        return grade;
    }

    private Map<String, String> getMapForQuestion(Map<String, String> formValues, String question) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> value : formValues.entrySet()) {
            if (value.getKey().contains(question)) {
                map.put(value.getKey(), value.getValue());
            }
        }
        return map;
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
