<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <style>
        .container {
            margin-top: 50px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="text-center">게시물 목록</h1>
        <table class="table table-hover text-center">
            <thead>
                <tr class="row table-info">
                    <th class="col">글 번호</th>
                    <th class="col">글 제목</th>
                    <th class="col">글쓴이</th>
                    <th class="col">쓴 날짜</th>
                    <th class="col">수정 날짜</th>
                    <th class="col">조회 수</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="blog" items="${pageInfo.getContent()}">
                    <tr class="row" onclick="location.href='/blog/detail/${blog.blogId}'">
                        <td class="col">${blog.blogId}</td>
                        <td class="col">${blog.blogTitle}</td>
                        <td class="col">${blog.writer}</td>
                        <td class="col">${blog.publishedAt}</td>
                        <td class="col">${blog.updateAt}</td>
                        <td class="col">${blog.blogCount}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div>
            <div class="">
                <!-- 페이징 처리 -->
                <ul class="pagination justify-content-center">
                    <!-- 이전 페이지 버튼 -->
                    <!-- c:if 태그는 test 프로퍼티에 참, 거짓을 판단할 수 있는 식을 넣어주면 참인경우만 해당 요소를 표시합니다. -->
                    <c:if test="${startPageNum != 1}">
                        <li class="page-item">
                            <a class="page-link" href="/blog/list/page=${startPageNum-1}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </c:if>
                    
                    <!-- 버튼 리스트 -->
                    <c:forEach begin="${startPageNum}" end="${endPageNum}" var="btnNum">
                        <li class="page-item ${currentPageNum == btnNum ? 'active' : ''}"><a class="page-link" href="/blog/list/page=${btnNum}">${btnNum}</a></li>
                    </c:forEach>
                    

                    <!-- 다음 페이지 버튼 -->
                    <c:if test="${endPageNum != pageInfo.getTotalPages()}">
                        <li class="page-item">
                            <a class="page-link" href="/blog/list/page=${endPageNum+1}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </div>
            <div>
                <a href="/blog/insert">글쓰기</a>
            </div>
        </div>
    </div>
</body>
</html>