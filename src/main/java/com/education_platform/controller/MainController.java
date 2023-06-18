package com.education_platform.controller;
import com.education_platform.data.UserRepository;
import com.education_platform.model.Role;
import com.education_platform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model){
        System.out.println(user);
        User userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB != null){
            model.addAttribute("error", "User exist");
            return "registration";
        }
        user.setRoles(Set.of(Role.STUDENT));
        userRepository.save(user);



        return "redirect:/login";
    }


}
