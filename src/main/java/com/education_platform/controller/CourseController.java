package com.education_platform.controller;

import com.education_platform.dto.CourseDTO;
import com.education_platform.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public String getAllCourses(Model model){
        List<CourseDTO> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("courses/{id}")
    public String getCourse(@Param(value="id") Long id, Model model){
        return "course";
    }
}
