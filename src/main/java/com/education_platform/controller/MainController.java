package com.education_platform.controller;
import com.education_platform.data.UserCourseRepository;
import com.education_platform.dto.ShortCourseDTO;
import com.education_platform.model.UserCourse;
import com.education_platform.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
;import java.util.List;

@Controller
public class MainController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/userCourses")
    public String userCourses(@AuthenticationPrincipal UserDetails userDetails,  Model model){
        List<ShortCourseDTO> userCourses = courseService.getAllCoursesByUser(userDetails.getUsername());
        model.addAttribute("courses", userCourses);
        return "user_courses";
    }


}
