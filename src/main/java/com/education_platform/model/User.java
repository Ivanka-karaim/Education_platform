package com.education_platform.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="usr")
public class User {
    @Id
    @NotNull
    private String email;
    @NotNull
    private String name;
    @NotNull
    private String surname;



    @NotNull
    private String password;
    @Size(max=20)
    private String phone_number;

    private boolean enabled=true;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name="user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
