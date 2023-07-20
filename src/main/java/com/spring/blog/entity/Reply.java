package com.spring.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Entity는 불변성을 지키기 위해 Setter를 제공하지 않습니다.
// 한 번 생성된 데이터가 변경될 가능성을 없앱니다.
@Getter @ToString @Builder @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyId;

    @Column(nullable = false)
    private long blogId;
    @Column(nullable = false)
    private String replyWriter;
    @Column(nullable = false)
    private String replyContent;

    private LocalDateTime publishedAt;
    private LocalDateTime updateAt;

    @PrePersist
    public void setInsert(){
        this.publishedAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
//        this.publishedAt = LocalDateTime.parse("YYYY:mm:DD HH:MM:ss");
//        this.updateAt = LocalDateTime.parse("YYYY:mm:DD HH:MM:ss");
    }

    @PreUpdate
    public void setUpdate(){
        this.updateAt = LocalDateTime.now();
//        this.updateAt = LocalDateTime.parse("YYYY:mm:DD HH:MM:ss");
    }
}
