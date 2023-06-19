package com.education_platform.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortTestDTO {

    private Long id;
    private String title;

    private Time duration;

    private float maxGrade;
}
