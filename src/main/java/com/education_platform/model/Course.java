package com.education_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;


    @NotNull
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @NotNull
    @ManyToOne
    @JoinColumn(name="teacher_id")
    private User teacher;

    private String description;
}
