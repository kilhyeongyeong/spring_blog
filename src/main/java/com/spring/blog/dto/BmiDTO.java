package com.spring.blog.dto;

import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
public class BmiDTO {
    public double height;
    private double weight;
}
