package com.spring.blog.repository;

import com.spring.blog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogRepository {

    // 사전에 정의해야 하는 메서드들
    // 1. 테이블 생성
    void createBlogTable();

    // 2. 더미 데이터 입력
    void insertTestData();

    // 3. 테이블 삭제
    void dropBlogTable(); // 단위 테스트 종료 후 DB 초기화를 위해 테이블 삭제

    /////////////////////////////
    // 전체 데이터 조회 기능
    List<Blog> findAll();
}
