package com.education_platform.model;

import org.springframework.security.core.GrantedAuthority;

public enum TestType  {
    ONE_ANSWER, FEW_ANSWER, WRITTEN_ANSWER;

    public String getName(){
        return name();
    }
}
