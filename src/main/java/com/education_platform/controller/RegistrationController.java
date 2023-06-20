package com.education_platform.controller;

import com.education_platform.data.UserRepository;
import com.education_platform.model.Role;
import com.education_platform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }
    @PostMapping("/registration")
    public String addUser(User user, @RequestParam(name="is_teacher") boolean is_teacher, Model model){
        System.out.println(user);
        User userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB != null){
            model.addAttribute("error", "User exist");
            return "registration";
        }
        if (is_teacher){
            user.setRoles(Set.of(Role.STUDENT, Role.TEACHER));
        }else {
            user.setRoles(Set.of(Role.STUDENT));
        }
        userRepository.save(user);
        return "redirect:/login";
    }

}