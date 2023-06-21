package com.education_platform.service;

import com.education_platform.data.UserRepository;
import com.education_platform.dto.UserDTO;
import com.education_platform.dto.Error;
import com.education_platform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public UserDTO getUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        return  parsingUserDTO(user);

    }
    public List<Error> validationData(User user, String passwordRepeat){
        List<Error> errors = new ArrayList<>();
        String regexAlpha = "^[A-Za-zА-ЩЬЮЯҐЄІЇа-щьюяґєії]+$";
        String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        String regexPhoneNumber = "^[+]?[0-9]{1,3}-?[0-9]{3,14}$";
        if (!Pattern.compile(regexAlpha).matcher(user.getName()).find()){
            errors.add(new Error("name", "The name must consist only of letters of the Ukrainian and English alphabets"));
        }
        if (!Pattern.compile(regexAlpha).matcher(user.getSurname()).find()){
            errors.add(new Error("surname", "The name must consist only of letters of the Ukrainian and English alphabets"));
        }
        if (!Pattern.compile(regexEmail).matcher(user.getSurname()).find()){
            errors.add(new Error("email", "The email was entered incorrectly"));
        }
        if (!Pattern.compile(regexPhoneNumber).matcher(user.getPhone_number()).find()){
            errors.add(new Error("phone_number", "The phone number was entered incorrectly"));
        }
        errors.addAll(validationPassword(user.getPassword(), passwordRepeat));

        return errors;
    }

    private List<Error> validationPassword(String password1, String password2){
        List<Error> errors = new ArrayList<>();
        if (!password1.equals(password2)){
            errors.add(new Error("password2", "Passwords do not match"));
        }
        String lowercaseRegex = "(?=.*[a-z])";
        String uppercaseRegex = "(?=.*[A-Z])";
        String digitRegex = "(?=.*\\d)";

        if (!Pattern.compile(lowercaseRegex).matcher(password1).find()){
            errors.add(new Error("password", "Password must contain at least one small letter (a-z)"));
        }
        if (!Pattern.compile(uppercaseRegex).matcher(password1).find()){
            errors.add(new Error("password", "Password must contain at least one big letter (A-Z)"));
        }
        if (!Pattern.compile(digitRegex).matcher(password1).find()){
            errors.add(new Error("password", "Password must contain at least one number (0-9)"));
        }
        if (password1.length()<8){
            errors.add(new Error("password", "Password must contain at least 8 digits"));
        }
        return errors;
    }



    private UserDTO parsingUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .roles(user.getRoles())
                .phone_number(user.getPhone_number())
                .build();

    }
}
