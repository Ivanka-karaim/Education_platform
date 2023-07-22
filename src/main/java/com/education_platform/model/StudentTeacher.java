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
@Table(name="student_teacher")
public class StudentTeacher {

    public StudentTeacher(UserCourse userCourse, CourseTeacher courseTeacher){
        this.userCourse = userCourse;
        this.courseTeacher = courseTeacher;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_course_id")
    private UserCourse userCourse;

    @ManyToOne
    @JoinColumn(name="course_teacher_id")
    private CourseTeacher courseTeacher;

}
