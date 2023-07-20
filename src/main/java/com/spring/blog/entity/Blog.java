package com.spring.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Date;
import java.time.LocalDateTime;

// 역질렬화(디비 -> 자바객체)
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder // 빌터 패턴 생성자를 쓸수 있게 해줌
@Entity
//@DynamicInsert  // null인 필드값이 기본값 지정된 요소를 집어넣어줌
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long   blogId; // 숫자는 어지간 하면 long형을 사용

    @Column(nullable = false)
    private String writer;
    @Column(nullable = false)
    private String blogTitle;
    @Column(nullable = false)

    private String blogContent;
    private LocalDateTime   publishedAt;
    private LocalDateTime updateAt;

    @ColumnDefault("0") // 카운트는 기본값을 0으로 설정
    private long blogCount;
    
    /*
    이렇게 사용할 수도 있음
    @Builder
    생성자()
     */

//    @PrePersist 어노테이션은 insert 가 되기 전 실행할 로직을 작성
    @PrePersist
    public void setBlogCount() {
//        this.blogContent = this.blogContent == null ? "0" : this.blogContent;
        this.publishedAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

//    @PreUpdate 어노테이션은 update 가 되기 전 실행할 로직을 작성
    @PreUpdate
    public void setUpdateValue() {
        this.updateAt = LocalDateTime.now();
    }
}
