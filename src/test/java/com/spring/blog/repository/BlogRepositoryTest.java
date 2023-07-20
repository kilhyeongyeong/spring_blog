package com.spring.blog.repository;

import com.spring.blog.entity.Blog;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS) // DROP TABLE시 필요한 어노테이션 - AfterAll일때만 필요
@TestInstance(TestInstance.Lifecycle.PER_METHOD) // -AfterEach일때 필요
public class BlogRepositoryTest {

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach // 각 테스트 전에 공통적으로 실행할 코드를 저장해 두는 곳
    public void setBlogRepository(){
        blogRepository.createBlogTable(); // blog 테이블 생성
        blogRepository.insertTestData();  // 생성된 blog 테이블에 더미데이터 3개 입력
    }

    @Test
    @DisplayName("전체 행을 얻어오고, 그 중 자바 1번 인덱스 행만 추출해 번호 확인")
    public void findAllTest(){
        // given - 2번 요소 조회를 위한 fixture 선언
        int blogId = 1;

        // when DB에 있는 모든 데이터를 자바 엔터디로 역직렬화
        List<Blog> blogList = blogRepository.findAll();
        System.out.println(blogList);
        
        // then
        // 1. 더미데이터가 3개 이므로 3개일 것이라 단언
        assertEquals(3, blogList.size());
        // 2. 2번째 객체의 ID번호는 2번일 것이다.
        assertEquals(2, blogList.get(blogId).getBlogId());
    }

    @Test
    @DisplayName("2번 글을 조회했을 때, 제목과 글쓴이와 번호가 단언대로 받아와지는지 확인")
    public void findByIdTest(){
        // givne
        long blogId = 2;
        // when
        Blog blog = blogRepository.findById(blogId);
        // then
        assertEquals("2번 유저", blog.getWriter());
        assertEquals("2번 제목", blog.getBlogTitle());
        assertEquals(2, blog.getBlogId());
    }

    @Test
    @DisplayName("4번째 행 데이터 저장 후, 행 저장여부 및 전달데이터 저장 여부 확인7")
    public void saveTest(){
        // given
        String writer = "오홍";
        String blogTitle = "아항";
        String blogContent = "유후";

        Blog blog = new Blog();
        blog.setWriter(writer);
        blog.setBlogTitle(blogTitle);
        blog.setBlogContent(blogContent);

        // when
        blogRepository.save(blog);
        List<Blog> blogList = blogRepository.findAll();

        // then
        assertEquals(4, blogList.size());
        assertEquals(writer, blogList.get(3).getWriter());
        assertEquals(blogTitle, blogList.get(3).getBlogTitle());
        assertEquals(blogContent, blogList.get(3).getBlogContent());
    }

    @Test
    @DisplayName("saveTest()를 빌더 패턴으로 리팩토링")
    public void refactoringBuilderPatternSaveTest(){
        // given
        String writer = "오홍";
        String blogTitle = "아항";
        String blogContent = "유후";

        // 빌더 패턴
        // 생성자에서 필드들을 초기화 해 줄 때 좀 더 간편 하게 생성자를 사용할 수 있게 해줍니다.
        // 생성자에서 set해주는 소스는 파라미터의 순서를 지켜야 하지만 builder 패턴을 사용할 경우 생성자에서 지정한 파라미터 대로 넣지 않아도 된다.
        // 장점 : 파라미터 순서를 뒤바꿔서 집어넣어도 상관 없음
        //       코드의 가독성이 좋음
        //       필요한 필드들만 초기화 해 줄수 있음
        Blog blog = Blog.builder()
                        .writer(writer)
                        .blogTitle(blogTitle)
                        .blogContent(blogContent)
                        .build(); // 빌더패턴 끝

        // when
        blogRepository.save(blog);
        List<Blog> blogList = blogRepository.findAll();

        // then
        assertEquals(4, blogList.size());
        assertEquals(writer, blogList.get(3).getWriter());
        assertEquals(blogTitle, blogList.get(3).getBlogTitle());
        assertEquals(blogContent, blogList.get(3).getBlogContent());
    }

    @Test
    @DisplayName("2번글 삭제 후 전체 목록을 가져왔을 때 남은 행 수는 2개. 삭제한 번호 재 조회시 null")
    public void deleteByIdTest(){
        // given
        long blogId = 2;
        // when
        blogRepository.deleteById(blogId);
        List<Blog> blogList = blogRepository.findAll();
        Blog blog = blogRepository.findById(blogId);
        // then
        assertEquals(2, blogList.size());
        assertNull(blog);
    }


    @Test
    public void updateTest(){
        // given
        long blogId = 1;
        String title = "아하";
        String content = "유후";

        // when
//        Blog blog = blogRepository.findById(blogId);
//        blog.setBlogTitle(title);
//        blog.setBlogContent(content);

        Blog blog = Blog.builder()
                        .blogId(blogId)
                        .blogTitle(title)
                        .blogContent(content)
                        .build();

        blogRepository.update(blog);
        blog = blogRepository.findById(blogId);

        // then
        assertEquals(title, blog.getBlogTitle());
        assertEquals(content, blog.getBlogContent());
    }

    @AfterEach // 각 단위테스트 끝난 후에 실행할 구문을 작성
    public void dropBlogTable(){
        blogRepository.dropBlogTable();
    }

}
