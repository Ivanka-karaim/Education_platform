package com.education_platform.dto;

import com.education_platform.model.Course;
import com.education_platform.model.Lecture;
import com.education_platform.model.Test;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModuleDTO {
    private Long id;

    private String title;

    private int duration;

    private List<LectureDTO> lectures;
    private int number;

    private ShortTestDTO test;

    private Long course_id;

}
