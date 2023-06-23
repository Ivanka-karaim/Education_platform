package com.education_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="user_test")
public class UserTest {

    public UserTest(float grade, User user, Test test){
        this.grade = grade;
        this.user = user;
        this.test = test;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    private float grade;

    @ManyToOne
    @JoinColumn(name="user_email")
    private User user;

    @ManyToOne
    @JoinColumn(name="test")
    private  Test test;


}
