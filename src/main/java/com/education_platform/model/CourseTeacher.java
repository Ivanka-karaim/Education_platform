package com.education_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="course_teacher")
public class CourseTeacher {

    @Id
    @NotNull
    private String id;

    @ManyToOne
    @JoinColumn(name="teacher_email")
    private User teacher;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;
}
