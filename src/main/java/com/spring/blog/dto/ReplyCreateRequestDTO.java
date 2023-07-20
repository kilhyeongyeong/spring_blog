package com.spring.blog.dto;

import com.spring.blog.entity.Reply;
import lombok.*;

@Getter @Setter @Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class ReplyCreateRequestDTO {
    private long blogId;
    private String replyWriter;
    private String replyContent;

    public ReplyCreateRequestDTO(Reply reply){
        this.blogId = reply.getBlogId();
        this.replyWriter = reply.getReplyWriter();
        this.replyContent = reply.getReplyContent();
    }
}
