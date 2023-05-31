package com.spring.blog.entity;

import lombok.*;

import java.sql.Date;

// 역질렬화(디비 -> 자바객체)
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder // 빌터 패턴 생성자를 쓸수 있게 해줌
public class Blog {
    private long   blogId; // 숫자는 어지간 하면 long형을 사용
    private String writer;
    private String blogTitle;
    private String blogContent;
    private Date   publishedAt;
    private Date   updateAt;
    private long   blogCount;
}
