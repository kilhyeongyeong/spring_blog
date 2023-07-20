package com.spring.blog.service;

import com.spring.blog.entity.Blog;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlogService {

    // 비즈니스 로직을 담당할 메서드를 "정의"만 하면 됩니다.
    Page<Blog> findAll(Long page);

    Blog findById(long blogId);

    void deleteById(long blogId);

    void save(Blog blog);

    void update(Blog blog);
}
