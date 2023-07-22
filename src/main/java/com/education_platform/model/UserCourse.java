package com.education_platform.model;

import com.mysql.cj.jdbc.Blob;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.rowset.serial.SerialBlob;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="user_course")
public class UserCourse {

    public UserCourse(User user, Course course){
        this.user = user;
        this.course = course;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name="date_start")
    private LocalDateTime dateStart = LocalDateTime.now();

    @Lob
    private SerialBlob certified=null;

    @ManyToOne
    @JoinColumn(name="user_email")
    private User user;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;
}
