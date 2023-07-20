package com.spring.blog.service;

import com.spring.blog.entity.Blog;
import com.spring.blog.repository.BlogJPARepository;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.ReplyJPARepository;
import com.spring.blog.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService{

    BlogRepository blogRepository;

    ReplyRepository replyRepository;

    BlogJPARepository blogJPARepository;

    ReplyJPARepository replyJPARepository;

    @Autowired // 생성자 주입이 속도가 더 빠름
    public BlogServiceImpl(BlogRepository blogRepository, ReplyRepository replyRepository, BlogJPARepository blogJPARepository, ReplyJPARepository replyJPARepository){
        this.blogRepository = blogRepository;
        this.replyRepository = replyRepository;
        this.blogJPARepository = blogJPARepository;
        this.replyJPARepository = replyJPARepository;
    }

    @Override
    public Page<Blog> findAll(Long page){
//        return blogRepository.findAll();    // Mybatis
//        return blogJPARepository.findAll();   // JPA

        long totalPagesCount = (long)Math.ceil(blogJPARepository.count() / 10.0);

        if(page == null || page <= 0)
            page = 1L;
        else if(page > totalPagesCount)
            page = totalPagesCount;

        // 페이징 처리에 관련된 정보를 먼저 객체로 생성
        Pageable pageable = PageRequest.of((int)(page-1) , 10);

        // 생성된 페이지 정보를 파라미터로 제공해서 findAll()을 호출합니다.
        return blogJPARepository.findAll(pageable);
    }

    @Override
    public Blog findById(long blogId) {
//        return blogRepository.findById(blogId);
        // JPA의 findById는 Optional을 리턴하므로 일반 객체로 만들기 위해 뒤에 .get()을 사용합니다.
        // Optional은 참조형 변수에 대해서 null 검사 및 처리를 쉽게 할 수 있도록 제공하는 제네릭입니다.
        // JPA는 Optional 을 사용하는 것을 권장하기 위해 리턴 자료형으로 강제해두었습니다.
        return blogJPARepository.findById(blogId).get();
    }

    @Transactional // 둘 다 실행되던지 둘 다 실행 안되던지
    @Override
    public void deleteById(long blogId) {
//        replyRepository.deleteByBlogId(blogId);
//        blogRepository.deleteById(blogId);
        blogJPARepository.deleteById(blogId);
        replyJPARepository.deleteByBlogId(blogId);
    }

    @Override
    public void save(Blog blog) {
//        blogRepository.save(blog);
        blogJPARepository.save(blog);
    }

    @Override
    public void update(Blog blog) {
        // JAP의 수정은 , findById() 를 이용해 얻어온 엔터티 클래스의 객체 내부 내용물을 수저앟ㄴ 다음
        // 해당 요소를 save() 해서 이뤄집니다.
        Blog blog_update = findById(blog.getBlogId());

        blog_update.setBlogTitle(blog.getBlogTitle());
        blog_update.setBlogContent(blog.getBlogContent());

        blogJPARepository.save(blog_update);
    }
}
