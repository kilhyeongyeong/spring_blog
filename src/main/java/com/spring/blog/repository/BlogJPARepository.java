package com.spring.blog.repository;

import com.spring.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository // bean container 에서 관리하도록 해야 사용 가능
public interface BlogJPARepository extends JpaRepository<Blog, Long> {

    // 페이징 정보를 받는 findAll 메서드 오버로딩 정의
    Page<Blog> findAll(Pageable pageable);

}
