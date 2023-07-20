package com.spring.blog.repository;

import com.spring.blog.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyJPARepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByBlogId(Long blogId);

    void deleteByBlogId(Long blogId);
}
