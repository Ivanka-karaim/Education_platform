package com.education_platform.controller;

import com.education_platform.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SendCodeController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/sendCode")
    public String sendCode(Model model, @RequestParam String course_id, @AuthenticationPrincipal UserDetails userDetails){
        model.addAttribute("code", teacherService.getCode(Long.valueOf(course_id), userDetails.getUsername()));

        return "code";
    }

    @GetMapping("/joinClass")
    public String joinClassPage(){
        return "join_class";
    }

    @PostMapping("/joinClass")
    public String joinClass(Model model, @RequestParam String code, @AuthenticationPrincipal UserDetails userDetails){
        Long course_id = teacherService.joinStudentInClass(code, userDetails.getUsername());
        return "redirect:/courses/"+course_id;
    }
}
