package com.spring.blog.exception;

public class NotFoundBlogException extends RuntimeException{
    // 생성자에 에러 사유를 전달할 수 있도록 메세지 출력
    public NotFoundBlogException(String message){
        super(message);
    }
}
