package com.spring.blog.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString @Builder
public class PersonDTO {
    private long id;
    private String name;
    private int age;

}
