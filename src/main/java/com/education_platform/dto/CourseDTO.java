package com.education_platform.dto;

import com.education_platform.model.User;

import lombok.*;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDTO {
    private Long id;
    private String name;
    private String category;
    private User teacher;
    private String description;

    private List<ShortModuleDTO> modules;
    private User user_enroll;

}
