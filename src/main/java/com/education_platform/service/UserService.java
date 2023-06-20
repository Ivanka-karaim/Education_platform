package com.education_platform.service;

import com.education_platform.data.UserRepository;
import com.education_platform.dto.UserDTO;
import com.education_platform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public UserDTO getUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        return  parsingUserDTO(user);

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
