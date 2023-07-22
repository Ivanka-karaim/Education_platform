package com.education_platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="student_class")
public class StudentClass {

    public StudentClass(UserCourse userCourse, Class aClass){
        this.userCourse = userCourse;
        this.aClass = aClass;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_course_id")
    private UserCourse userCourse;

    @ManyToOne
    @JoinColumn(name="course_teacher_id")
    private Class aClass;

}
