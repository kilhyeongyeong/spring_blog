package com.spring.blog.repository;

import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReplyRepositoryTest {

    @Autowired // 테스트 코드에서는 필드 주입을 써도 무방합니다.
    ReplyRepository repository;

    @Test
    @Transactional
    @DisplayName("2번 글에 연동된 댓글 개수가 4개인지 확인")
    public void findAllByBlogIdTest(){
        // given : 2번 글을 조회하기 위한 fixture 지ㅓ장
        long blogId = 2;
        //when : findAllByBlogId() 호출 및 결과 자료 저장
        List<ReplyResponseDTO> replyFindAllByBlogIdDTOList = repository.findAllByBlogId(blogId);
        // then : 2번 글에 연동된 댓글이 4개일것이라고 단언
        assertEquals(4, replyFindAllByBlogIdDTOList.size());
        assertThat(replyFindAllByBlogIdDTOList.size()).isEqualTo(4);
    }

    @Test
    @Transactional
    @DisplayName("댓글 번호 3번 자료의 글쓴이는 헬로")
    public void findByReplyIdTest(){
        long replyId = 3;
        ReplyResponseDTO dto = repository.findByReplyId(replyId);
        assertThat("헬로").isEqualTo(dto.getReplyWriter());
    }

    @Test
    @Transactional
    @DisplayName("2번 댓글 삭제후 전체 대이터 개수가 4개로, 2번 재 조회시 null일 것이다.")
    public void deleteByReplyIdTest(){
        long replyId = 2;
        long blogId = 2;
        repository.deleteByRelplyId(replyId);

        assertEquals(3, repository.findAllByBlogId(blogId).size());
        assertNull(repository.findByReplyId(replyId));
    }

    @Test
    @Transactional
    @DisplayName("2번 댓글 삭제 후 전체 데이터 개수가 0개")
    public void deleteByBlogIdTest(){
        long blogId = 2;

        repository.deleteByBlogId(blogId);

        assertEquals(0, repository.findAllByBlogId(blogId).size());
    }

    @Test
    @Transactional
    @DisplayName("insert")
    public void saveTest(){
        long blogId = 2;
        String writer = "하잉";
        String content = "오와아아아아아앙";

        ReplyCreateRequestDTO dto = ReplyCreateRequestDTO.builder()
                .blogId(blogId)
                .replyContent(content)
                .replyWriter(writer)
                .build();
        repository.save(dto);

        List<ReplyResponseDTO> list = repository.findAllByBlogId(blogId);

        assertEquals(5, list.size());
        assertEquals(writer, list.get(list.size()-1).getReplyWriter());
        assertEquals(content, list.get(list.size()-1).getReplyContent());
    }

    @Test
    @Transactional
    @DisplayName("update")
    public void updateTest(){
        long replyId = 3;
        String writer = "하잉";
        String content = "오와아아아아아앙";

        ReplyUpdateRequestDTO dto = ReplyUpdateRequestDTO.builder()
                .replyId(replyId)
                .replyContent(content)
                .replyWriter(writer)
                .build();
        repository.update(dto);

        ReplyResponseDTO replyResponseDTO = repository.findByReplyId(replyId);
        assertNotEquals(replyResponseDTO.getPublishedAt(), replyResponseDTO.getUpdateAt());
    }
}
