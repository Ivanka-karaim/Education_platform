package com.education_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="user_answer")
public class UserAnswer {

    public UserAnswer(String answerText, Answer answer, User user){
        this.answerText = answerText;
        this.answer = answer;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="answer_text")
    private String answerText;

    @ManyToOne
    @JoinColumn(name="answer_id")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


}
