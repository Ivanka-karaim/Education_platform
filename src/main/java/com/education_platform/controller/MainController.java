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
public class MainController {

    @GetMapping("/")
    public String home(){
        return "home";
    }



}
