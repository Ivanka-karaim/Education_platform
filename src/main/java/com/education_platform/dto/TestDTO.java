package com.education_platform.dto;

import com.education_platform.model.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestDTO {
    private Long id;
    private String title;

    private Time duration;
    private Long seconds;

    private String time;

    private float maxGrade;

    private List<QuestionDTO> questions;

}
