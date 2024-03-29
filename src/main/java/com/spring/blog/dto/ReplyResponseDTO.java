package com.spring.blog.dto;

import com.spring.blog.entity.Reply;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class ReplyResponseDTO {
    private long replyId;
    private String replyWriter;
    private String replyContent;
    private LocalDateTime publishedAt;
    private LocalDateTime updateAt;

    // DTO는 Entity 객체를 이용해서 생성될 수 있어야 하나
    // 반대는 성립하지 않는다.(Entity는 DTO의 내부 구조를 알 필요가 있다.)
    public ReplyResponseDTO(Reply reply){
        this.replyId        = reply.getReplyId();
        this.replyWriter    = reply.getReplyWriter();
        this.replyContent   = reply.getReplyContent();
        this.publishedAt    = reply.getPublishedAt();
        this.updateAt       = reply.getUpdateAt();

    }
}
