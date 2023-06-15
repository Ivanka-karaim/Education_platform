package com.education_platform.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="user")
public class User {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String surname;

    @NotNull
    private String email;

    @NotNull
    private String password;
    @Size(max=20)
    private String phone_number;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
