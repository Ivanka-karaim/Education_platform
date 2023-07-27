package com.education_platform.service;

import com.education_platform.data.*;
import com.education_platform.dto.AnswerDTO;
import com.education_platform.dto.ShortTestDTO;
import com.education_platform.dto.TestDTO;
import com.education_platform.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    UserCourseRepository userCourseRepository;



    public List<ShortTestDTO> getTestDTOByModule(Long id) {
        List<Test> test = testRepository.findByModuleId(id);
        return parsingShortTestDTO(test);
    }

    public  List<UserTest> getAllUserTestByCourseAndUser(String user_id, Long course_id){
        return userTestRepository.findAllByUserEmailAndTest_Module_CourseId(user_id, course_id);
    }

    public UserTest userFinishedTest(String user_id, Long test_id) {
        return userTestRepository.findByUserEmailAndTestId(user_id, test_id).orElse(null);
    }


    public TestDTO getTestDTOById(Long id) {
        Test test = testRepository.findById(id).orElse(new Test());
        return parsingTestDTO(test);
    }

    public ShortTestDTO getShortTestDTOById(Long id) {
        Test test = testRepository.findById(id).orElse(new Test());
        return parsingShortTestDTO(List.of(test)).get(0);
    }

    public float resultTest(Map<String, String> formValues,String user_id) {
        formValues.remove("_csrf");
        Long question = Long.valueOf("0");
        float grade = 0;
        User user = userRepository.findByEmail(user_id).orElse(new User());
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

        UserCourse userCourse = userCourseRepository.findByUserEmailAndCourseId(user_id, userTest.getTest().getModule().getCourse().getId()).orElse(new UserCourse());
        userCourse.setGrade(userCourse.getGrade()+grade);
        userCourseRepository.save(userCourse);

        if (getProgressByCourseAndUser(userTest.getTest().getModule().getCourse().getId(), user_id) == 1){
            userCourse.setCertificate(true);
            userCourseRepository.save(userCourse);
        }
        return grade;
    }
    public float getProgressByCourseAndUser(Long course_id, String user_id) {
        List<Test> tests = testRepository.findAllByModule_CourseId(course_id);
        List<UserTest> userTests = getAllUserTestByCourseAndUser(user_id, course_id);
        return tests.size() == 0 ? 1 : (float) userTests.size() / tests.size();
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

    private List<ShortTestDTO> parsingShortTestDTO(List<Test> tests) {
        List<ShortTestDTO> testDTOs = new ArrayList<>();

        for (Test test: tests) {
            testDTOs.add(ShortTestDTO.builder()
                    .id(test.getId())
                    .duration(test.getDuration())
                    .maxGrade(test.getMaxGrade())
                    .title(test.getTitle())
                    .build());
        }
        return testDTOs;

    }

    private TestDTO parsingTestDTO(Test test) {
        return TestDTO.builder()
                .id(test.getId())
                .duration(test.getDuration())
                .maxGrade(test.getMaxGrade())
                .title(test.getTitle())
                .time(formatTime(test.getDuration()))
                .seconds(TimeUnit.HOURS.toSeconds(test.getDuration().getHours())
                        + TimeUnit.MINUTES.toSeconds(test.getDuration().getMinutes())
                        + test.getDuration().getSeconds())
                .questions(questionService.getQuestionDTOsByTest(test.getId()))
                .build();

    }

    private static String formatTime(Time time) {
        long totalSeconds = time.getTime() / 1000;
        System.out.println(totalSeconds);


        long hours = time.getHours();
        long minutes = time.getMinutes();
        long seconds = time.getSeconds();

        StringBuilder sb = new StringBuilder();

        if (hours > 0) {
            sb.append(hours).append(" год ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" хв ");
        } if (seconds>0) {
            sb.append(seconds).append(" сек");
        }

        return sb.toString();
    }
}
