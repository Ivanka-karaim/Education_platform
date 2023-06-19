package com.education_platform.controller;

import com.education_platform.dto.*;
import com.education_platform.service.CourseService;
import com.education_platform.service.LectureService;
import com.education_platform.service.ModuleService;
import com.education_platform.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private TestService testService;

    @GetMapping("/courses")
    public String getAllCourses(Model model){
        List<ShortCourseDTO> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("/courses/{id}")
    public String getCourse(@PathVariable("id") Long id, Model model){
        CourseDTO course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "course";
    }

    @GetMapping("/courses/{id}/modules")
    public String getModulesByCourse(@PathVariable("id") Long id, Model model){
        List<ShortModuleDTO> moduleDTOS = moduleService.getModuleDTOsByCourse(id);
        model.addAttribute("modules", moduleDTOS);
        return "modules";
    }

    @GetMapping("/modules/{id_module}")
    public String getModuleById( @PathVariable("id_module") Long id_module, Model model){
        ModuleDTO moduleDTO = moduleService.getModuleById(id_module);
        model.addAttribute("module", moduleDTO);

        List<ShortModuleDTO> moduleDTOS = moduleService.getModuleDTOsByCourse(moduleDTO.getCourse_id());

        int index = moduleDTOS.indexOf(new ShortModuleDTO(moduleDTO.getId(), moduleDTO.getTitle(), moduleDTO.getDuration()));
        if (index == 0 ){
            model.addAttribute("next", moduleDTOS.get(index+1).getId());
        }else if (index == moduleDTOS.size()-1){
            model.addAttribute("prev", moduleDTOS.get(index-1).getId());
        }else{
            model.addAttribute("next", moduleDTOS.get(index+1).getId());
            model.addAttribute("prev", moduleDTOS.get(index-1).getId());

        }
        return "module";
    }

    @GetMapping("/modules/{id_module}/lectures")
    public String getLecturesByModule(@PathVariable("id_module") Long id_module, Model model){
        List<LectureDTO> lectureDTOs = lectureService.getLectureDTOsByModule(id_module);
        model.addAttribute("lectures", lectureDTOs);
        return "lectures";
    }

    @GetMapping("/lectures/{id_lecture}")
    public String getLectureById(@PathVariable("id_lecture") Long id_lecture,  Model model){
        LectureDTO lectureDTO = lectureService.getLectureById(id_lecture);
        model.addAttribute("lecture", lectureDTO);

        List<LectureDTO> lectureDTOs = lectureService.getLectureDTOsByModule(lectureDTO.getModule_id());
        int index = lectureDTOs.indexOf(lectureDTO);
        if (index == 0 ){
            model.addAttribute("next", lectureDTOs.get(index+1).getId());
        }else if (index == lectureDTOs.size()-1){
            model.addAttribute("prev", lectureDTOs.get(index-1).getId());
        }else{
            model.addAttribute("next", lectureDTOs.get(index+1).getId());
            model.addAttribute("prev", lectureDTOs.get(index-1).getId());

        }

        return "lecture";
    }

    @GetMapping("/modules/{id_module}/test/")
    public String getInformTestById(@PathVariable("id_module") Long id_module,  Model model){
        ShortTestDTO shortTestDTO = testService.getTestDTOByModule(id_module);
        model.addAttribute("test", shortTestDTO);
        model.addAttribute("message","StartTest");
        return "info_test";
    }

    @GetMapping("/test/{id_test}")
    public String getTestById(@PathVariable("id_test") Long id_test,  Model model){
        TestDTO testDTO = testService.getTestDTOById(id_test);
        model.addAttribute("test", testDTO);
        return "test";
    }
}
