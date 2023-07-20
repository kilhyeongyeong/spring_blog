package com.spring.blog.service;

import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import com.spring.blog.entity.Reply;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class ReplyServiceTest {

    @Autowired
    ReplyService replyService;

    @Test
    @Transactional
    @DisplayName("2번 글에 연동된 댓글 개수가 4개인지 확인")
    public void findAllByBlogIdTest(){
        // given : 2번 글을 조회하기 위한 fixture 지ㅓ장
        long blogId = 2;
        //when : findAllByBlogId() 호출 및 결과 자료 저장
        List<Reply> replyFindAllByBlogIdDTOList = replyService.findAllByBlogId(blogId);
        // then : 2번 글에 연동된 댓글이 4개일것이라고 단언
        assertEquals(4, replyFindAllByBlogIdDTOList.size());
        assertThat(replyFindAllByBlogIdDTOList.size()).isEqualTo(4);
    }

    @Test
    @Transactional
    @DisplayName("댓글 번호 5번 자료의 글쓴이는 바이")
    public void findByReplyIdTest() {
        // given
        long replyId = 5;
        // when

        // then
        assertEquals("바이", replyService.findByReplyId(replyId).getReplyWriter());
    }

    @Test
    @Transactional
    @DisplayName("2번 댓글 삭제후 전체 대이터 개수가 4개로, 2번 재 조회시 null일 것이다.")
    public void deleteByReplyId() {
        // given
        long replyId = 2;
        long blogId = 2;
        // when
        replyService.deleteByReplyId(replyId);
        // then
        assertEquals(3, replyService.findAllByBlogId(blogId).size());
        assertNull(replyService.findByReplyId(replyId));
    }

    @Test
    @Transactional
    @DisplayName("저장 후 전체 데이터 갯수 조회")
    public void saveTest(){
        // given
        String writer = "오잉";
        String content = "오이이이이이잉?\n오오오오오옹???";
        long blogId = 3;
        // when
        Reply inserDTO = Reply
                .builder()
                .replyWriter(writer)
                .replyContent(content)
                .blogId(blogId)
                .build();
        replyService.save(inserDTO);
        // then
        assertEquals(2, replyService.findAllByBlogId(blogId).size());
    }

    @Test
    @Transactional
    @DisplayName("replyId 3번 수정")
    public void updateTest(){
        // given
        String writer = "이응";
        String content = "이이이이이이이이응";
        long replyId = 3;
        // when
        Reply updateDTO = Reply
                .builder()
                .replyWriter(writer)
                .replyContent(content)
                .replyId(replyId)
                .build();
        replyService.update(updateDTO);

        Reply findByIdDTO = replyService.findByReplyId(replyId);
        // then
        assertEquals(writer, findByIdDTO.getReplyWriter());
        assertEquals(content, findByIdDTO.getReplyContent());
        assertThat(findByIdDTO.getPublishedAt()).isBefore(findByIdDTO.getUpdateAt());
    }

}
