package com.spring.blog.service;

import com.spring.blog.entity.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class BlogServiceTest {
    @Autowired
    BlogService blogService;

    @Test
    @Transactional // 이 테스트의 결과가 디비 커밋을 하지 않음
    public void findAllTest(){
        // given - 없음
        // when - 전체 데이터 가져오기
//        Page<Blog> blogList = blogService.findAll();
        // then - 길이 = 3
        //assertEquals(3, blogList.size());
    }

    @Test
    @Transactional
    public void findByIdTest(){
        // given
        long blogId = 2;
        // when
        // then
        assertEquals("2번유저", blogService.findById(blogId).getWriter());
    }

    @Test
    @Transactional
    public void deleteByIdTest(){
        // given
        long blogId = 2;
        // when
        blogService.deleteById(blogId);
        // then
        //assertEquals(2, blogService.findAll().size());
        assertNull(blogService.findById(blogId));
    }

    @Test
    @Transactional
    public void saveTest(){
        // given
        String writer = "4번 유저";
        String title = "4번 제목";
        String content = "4번 내용";

        Blog blog = Blog.builder()
                        .writer(writer)
                        .blogTitle(title)
                        .blogContent(content)
                        .build();
        // when
        blogService.save(blog);
        // then
//        assertEquals(4, blogService.findAll().size());
    }

    @Test
    @Transactional
    public void updateTest(){
        // given
        long blogId = 1;
        String title = "모지 제목";
        String content = "모지모지 내용";

        Blog blog = blogService.findById(blogId);
        System.out.println(blog);
        // when
        blog.setBlogTitle(title);
        blog.setBlogContent(content);
//        blogService.update(blog);

        Blog bbb = blogService.findById(blogId);
        System.out.println(bbb);

//        Page<Blog> blogList1 = blogService.findAll();
//        Blog ccc = blogList1.get(0);
//        ccc.setBlogTitle("ㅇㄹㅇㄹㅇ");

//        Page<Blog> blogList = blogService.findAll();
//        System.out.println("확인 : " + blogList);

        Blog ddd = blogService.findById(1);
        System.out.println(ddd);

        // then
        assertEquals(title, bbb.getBlogTitle());
        assertEquals(content, bbb.getBlogContent());
    }

    // blog와 함께 reply가 삭제되는 케이스는 따로 다시 테스트코드를 하나 더 작성해주겍 좋음

}
