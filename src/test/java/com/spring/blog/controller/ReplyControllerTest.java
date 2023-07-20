package com.spring.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import com.spring.blog.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // MVC 테스트는 브라우저를 켜야 원래 테스트가 가능하므로 브라우저를 대체할 객체를 만들어 수행
class ReplyControllerTest {

    @Autowired // @AutoConfigureMockMvc 어노테이션이 없을 경우 주입이 안됨
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;
    
    @Autowired // 데이터 직렬화에 사용하는 객체
    private ObjectMapper objectMapper;

    // 임시적으로 Repository를 생성
    // 레포지토리의 메서드는 쿼리문을 하나만 호출하는것이 보장되지만
    // 서비스 레이어의 메서드는 추후에 쿼리문을 두 개 이상 호출 할 수도 있고, 그런 변경이 생겼을 때 테스트코드도 같이 수정할 가능성이 생김
    @Autowired
    private ReplyRepository repository;

    // 컨트롤러를 테스트 해야 하는데 컨트롤러는 서버에 url만 입력하면 동작하므로 컨트롤러를 따로 생성하지는 않습니다.
    // 각 테스트 전 Setting
    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Transactional
    @DisplayName("2번 글에 대한 전체 댓글을 조회했을 때, 0번째 요소의 replyWriter는 댓글쓴사람, replyId는 1")
    void findAllRepliesTest() throws Exception { // MockMVK의 예외를 던져줄 Exception 처리
        // given : fixture 설정
        String replyWriter = "댓글쓴사람";
        long replyId = 1L;
        String url = "/reply/2/all";

        // when : 위세 설정한 url 주소로 접속 후 ResultActions 형 자료로 json 저장하기
        // get() : alt + Enter -> MockMVC 관련 요소로 import

        // == fetch(url, { method:'get' }).then(result => result.json());
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        // then : 리턴받은 json 목록의 0번째 요소의 replyWriter와 replyId가 예상과 일치하는지 확인
        // assert와 같은 의미
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].replyWriter").value(replyWriter))
                .andExpect(jsonPath("$[0].replyId").value(replyId));

        //result.andExpect(jsonPath("$[0].replyWriter", equalTo(replyWriter)));

    }

    @Test
    @Transactional
    @DisplayName("3번 댓글 조회")
    void findByReplyIdTest() throws Exception {
        // given
        long replyId = 3;
        String replyWriter = "헬로";
        String url = "/reply/" + replyId;
        // when
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("replyId").value(replyId))
                .andExpect(jsonPath("replyWriter").value(replyWriter))
                .andExpect(jsonPath("$.replyWriter").value(replyWriter)); // 윗 줄과 같음
    }

    @Test
    @Transactional
    @DisplayName("blogId : 1번, replyWriter : 호잇, replyContent : 메에에에에에로오오옹\n메롱👻")
    public void insertReplyTest() throws Exception{
        // given
        long blogId = 1L;
        String writer = "호잇";
        String content = "메에에에에에로오오옹";
        String url = "/reply";
        String url2 = "reply/1/all";

        ReplyCreateRequestDTO inserDTO = new ReplyCreateRequestDTO(blogId, writer, content);
        
        // 데이터 직렬화 -- throws Exception 필요
        final String requestBody = objectMapper.writeValueAsString(inserDTO);
        
        // when
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON) // 전달 자료가 json이라는 의미
                        .content(requestBody)); // 직렬화한 requestBody 변수 전달
        // then
//        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
//
//        result
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].replyWriter").value(writer))
//                .andExpect(jsonPath("$[0].replyContent").value(content));
    }

    @Test
    @Transactional
    @DisplayName("댓글번호 3번을 삭제할 경우, 글버호 2번의 댓글수는 3개, 그리고 단일 댓글 조회시 null")
    public void deleteReplyTest() throws Exception{
        // given
        long replyId = 3;
        long blogId = 2;
        String url = "http://localhost:8080/reply/" + replyId;

        // when
        mockMvc.perform(delete(url));

        // then
        assertEquals(3, repository.findAllByBlogId(blogId).size());
        assertNull(repository.findByReplyId(replyId));

    }

    @Test
    @Transactional
    @DisplayName("댓글 번호 1번 updqte")
    public void updateReplyTest() throws Exception {
        // given
        long replyId = 4L;
        String writer = "냥냥이";
        String content = "메롱메롱메에에에에롱👻";
        String url = "http://localhost:8080/reply/" + replyId;

        ReplyUpdateRequestDTO updateRequestDTO = ReplyUpdateRequestDTO
                .builder()
                .replyWriter(writer)
                .replyContent(content)
                .build();
        final String requestBody = objectMapper.writeValueAsString(updateRequestDTO);

        // when
        mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        final ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("replyWriter").value(writer))
                .andExpect(jsonPath("replyContent").value(content));
    }
}