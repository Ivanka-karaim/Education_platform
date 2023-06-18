package com.education_platform.model;

import jakarta.persistence.*;
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
@Entity
@Table(name="test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Time duration;

    @JoinColumn(name="max_grade")
    private float maxGrade;

    @ManyToOne
    @JoinColumn(name="module_id")
    private Module module;


}
