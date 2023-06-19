package com.education_platform.dto;

import com.education_platform.model.User;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortCourseDTO {
    private Long id;
    private String name;
    private String category;
    private User teacher;
    private String description;

}
