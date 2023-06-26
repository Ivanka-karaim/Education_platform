package com.education_platform.controller;

import com.education_platform.dto.ShortCourseDTO;
import com.education_platform.dto.UserDTO;
import com.education_platform.model.User;
import com.education_platform.service.CourseService;
import com.education_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal UserDetails userDetails){
        UserDTO user = userService.getUserByEmail(userDetails.getUsername());
        List<ShortCourseDTO> courses = courseService.getAllCoursesByUser(userDetails.getUsername());

        model.addAttribute("user", user);
        model.addAttribute("courses", courses);

        return "user/profile";
    }

    @GetMapping("/edit_profile")
    public String editProfilePage(Model model, @AuthenticationPrincipal UserDetails userDetails){
        UserDTO user = userService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "user/edit_profile";
    }

    @PostMapping("/edit_profile")
    public String editProfile(@RequestParam Map<String, String> formValues, Model model, @AuthenticationPrincipal UserDetails userDetails){
        userService.editUser(new User(formValues.get("name"), formValues.get("surname"),  formValues.get("phone_number")), userDetails.getUsername());
        return "redirect:/user/profile";
    }
}
