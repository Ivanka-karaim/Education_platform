package com.education_platform.controller;

import com.education_platform.data.CategoryRepository;
import com.education_platform.dto.*;
import com.education_platform.model.Category;
import com.education_platform.model.UserTest;
import com.education_platform.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

    @Autowired
    private TestService testService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/courses")
    public String getAllCourses(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam(value = "category", required = false) Long categoryId, @RequestParam(value = "search", required = false) String searchQuery) {
        List<ShortCourseDTO> courses;
        if (userDetails != null) {
            UserDTO userDTO = userService.getUserByEmail(userDetails.getUsername());
            List<ShortCourseDTO> courses1 = courseService.getAllCoursesByUser(userDTO.getEmail());
            if (categoryId != null) {
                courses = courseService.getAllCoursesByCategory(categoryId, userDTO.getEmail());
            } else if (searchQuery != null) {
                courses = courseService.getAllCoursesSearch(searchQuery, userDTO.getEmail());
            } else {
                courses = courseService.getAllCourses(userDTO.getEmail());
            }
            for(ShortCourseDTO course: courses){
                System.out.println(courses1.contains(course));
            }
            model.addAttribute("userCourses", courses1);

        } else {
            if (categoryId != null) {
                courses = courseService.getAllCoursesByCategory(categoryId);
            } else if (searchQuery != null) {
                courses = courseService.getAllCoursesSearch(searchQuery);
            } else {
                courses = courseService.getAllCourses();
            }
        }
        model.addAttribute("search", searchQuery);

        List<Category> categories = (List<Category>) categoryRepository.findAll();
        model.addAttribute("categories", categories);

        model.addAttribute("courses", courses);
        return "courses";
    }

    @PostMapping("/change_enroll_courses")
    public String enrollCourse(@RequestParam String course_enroll, @RequestParam String page, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO userDTO = userService.getUserByEmail(userDetails.getUsername());
        boolean userNotEnroll = courseService.enrollUserCourse(userDTO.getEmail(), Long.valueOf(course_enroll));
        return "redirect:" + page;
    }

    @GetMapping("/courses/{id}")
    public String getCourse(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        CourseDTO course;
        if (userDetails != null) {
            course = courseService.getCourseById(id, userService.getUserByEmail(userDetails.getUsername()).getEmail());
            float rating = courseService.getRatingByCourseAndUser(course.getId(), userService.getUserByEmail(userDetails.getUsername()).getEmail());
            System.out.println(rating);
            model.addAttribute("rating", rating * 100);
        } else {
            course = courseService.getCourseById(id);
        }

        model.addAttribute("course", course);
        return "course";
    }

    @GetMapping("/courses/{id}/modules")
    public String getModulesByCourse(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (!courseService.userHasCourse(userService.getUserByEmail(userDetails.getUsername()).getEmail(), id)) {
            model.addAttribute("error", "Спочатку зареєструйся");
            return getCourse(id, model, userDetails);
        }
        List<ShortModuleDTO> moduleDTOS = moduleService.getModuleDTOsByCourse(id);
        model.addAttribute("modules", moduleDTOS);
        return "modules";
    }

    @GetMapping("/modules/{id_module}")
    public String getModuleById(@PathVariable("id_module") Long id_module, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        ModuleDTO moduleDTO = moduleService.getModuleById(id_module);
        if (!courseService.userHasCourse(userService.getUserByEmail(userDetails.getUsername()).getEmail(), moduleDTO.getCourse_id())) {
            return "redirect:/courses/" + moduleDTO.getCourse_id();
        }
        model.addAttribute("module", moduleDTO);
        List<ShortModuleDTO> moduleDTOS = moduleService.getModuleDTOsByCourse(moduleDTO.getCourse_id());

        int index = moduleDTOS.indexOf(new ShortModuleDTO(moduleDTO.getId(), moduleDTO.getTitle(), moduleDTO.getDuration()));
        if (moduleDTOS.size() > 1) {
            if (index == 0) {
                model.addAttribute("next", moduleDTOS.get(index + 1).getId());
            } else if (index == moduleDTOS.size() - 1) {
                model.addAttribute("prev", moduleDTOS.get(index - 1).getId());
            } else {
                model.addAttribute("next", moduleDTOS.get(index + 1).getId());
                model.addAttribute("prev", moduleDTOS.get(index - 1).getId());

            }
        }
        return "module";
    }

    @GetMapping("/modules/{id_module}/lectures")
    public String getLecturesByModule(@PathVariable("id_module") Long id_module, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<LectureDTO> lectureDTOs = lectureService.getLectureDTOsByModule(id_module);
        if (!courseService.userHasCourse(userService.getUserByEmail(userDetails.getUsername()).getEmail(), moduleService.getModuleById(id_module).getCourse_id())) {
            return "redirect:/courses/" + moduleService.getModuleById(id_module).getCourse_id();
        }

        model.addAttribute("lectures", lectureDTOs);
        return "lectures";
    }

    @GetMapping("/lectures/{id_lecture}")
    public String getLectureById(@PathVariable("id_lecture") Long id_lecture, Model model) {
        LectureDTO lectureDTO = lectureService.getLectureById(id_lecture);
        model.addAttribute("lecture", lectureDTO);

        List<LectureDTO> lectureDTOs = lectureService.getLectureDTOsByModule(lectureDTO.getModule_id());
        int index = lectureDTOs.indexOf(lectureDTO);
        if (index == 0) {
            model.addAttribute("next", lectureDTOs.get(index + 1).getId());
        } else if (index == lectureDTOs.size() - 1) {
            model.addAttribute("prev", lectureDTOs.get(index - 1).getId());
        } else {
            model.addAttribute("next", lectureDTOs.get(index + 1).getId());
            model.addAttribute("prev", lectureDTOs.get(index - 1).getId());

        }

        return "lecture";
    }

    @GetMapping("/modules/{id_module}/test/{id_test}")
    public String getInformTestById(@PathVariable("id_module") Long id_module,@PathVariable("id_test") Long id_test, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        TestDTO testDTO = testService.getTestDTOById(id_module);
        UserTest userTest = testService.userFinishedTest(userService.getUserByEmail(userDetails.getUsername()).getEmail(), testDTO.getId());
        if (userTest != null) {
            model.addAttribute("error", "Тест вже пройдено");
            model.addAttribute("grade", userTest.getGrade());
            model.addAttribute("maxGrade", userTest.getTest().getMaxGrade());
        }
        model.addAttribute("test", testDTO);
        return "info_test";
    }

    @GetMapping("/test/{id_test}")
    public String getTestById(@PathVariable("id_test") Long id_test, Model model) {
        TestDTO testDTO = testService.getTestDTOById(id_test);
        model.addAttribute("test", testDTO);
        return "test";
    }

    @PostMapping("/test/{id_test}")
    public String formTest(@RequestParam Map<String, String> formValues, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        for (Map.Entry<String, String> entry : formValues.entrySet()) {
            System.out.println(entry.getKey() + "-" + entry.getValue());
        }
        float result = testService.resultTest(formValues, userService.getUserByEmail(userDetails.getUsername()).getEmail());

        model.addAttribute("result", result);
        return "result";
    }

}
