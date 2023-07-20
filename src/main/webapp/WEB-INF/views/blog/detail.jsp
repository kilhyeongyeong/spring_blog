<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <!-- JavaScript Bundle with Popper -->
    <style>
        .container {
            margin-top: 50px;
        }
        .first-row, .second-row, .third-row {
            border : 1px solid blueviolet;
        }
        .no-padding {
            padding: 0;;
        }
        #replies {
            margin: 10px 20px;
        }
        #replies>div:nth-child(2n) {
            border-bottom: 1px solid blueviolet;
        }
    </style>
</head>
<body>
    <!-- 모달 -->
    <div class="modal fade" id="replyUpdateModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h1 class="modal-title fs-5" id="exampleModalLabel">댓글 수정하기</h1>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              작성자     <input type="text" class="form-control" id="modalReplyWriter"><br>
              댓글 내용  <input type="text" class="form-control" id="modalReplyContent">
              <input type="hidden" id="modalReplyId" value="">
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
              <button type="button" class="btn btn-primary" data-bs-dismiss="modal" id="replyUpdateBtn">수정하기</button>
            </div>
          </div>
        </div>
      </div>

    <div class="container">
        <div class="row first-row">
            <div class="col-2">글 번호</div>
            <div class="col-4">${blog.blogId}</div>
            <div class="col-2">글 제목</div>
            <div class="col-4">${blog.blogTitle}</div>
        </div><!-- .first-row -->
        <div class="row second-row">
            <div class="col-2">작성일</div>
            <div class="col-4">${blog.publishedAt}</div>
            <div class="col-2">수정일</div>
            <div class="col-4">${blog.updateAt}</div>
        </div>
        <div class="row third-row">
            <div class="col-2">본문</div>
            <div class="col-4">${blog.blogContent}</div>
            <div class="col-2">조회수</div>
            <div class="col-4">${blog.blogCount}</div>
        </div>
        <div class="row">
            <div id="replies" class="col-11">
                
            </div>
        </div>
        <div class="row">
            <!-- 비동기 form의 경우는 목적지로 이동하지 않고 페이지 내에서 처리가 되므로 action을 가지지 않습니다.
            그리고 제출버튼도 제출기능을 막고 fetch요청만 넣습니다. -->
            <div class="col-1">
                <input type="text" class="form-control" name="replyWriter" placeholder="작성자" id="replyWriter">
            </div>
            <div class="col-6">
                <input type="text" class="form-control" name="replyContent" placeholder="내용" id="replyContent">
            </div>
            <div class="col-1">
                <button class="btn btn-primary" id="replySubmit" style="width: 100px !important;">댓글 작성</button>
            </div>
        </div>
        <div class="row" style="margin: 10px 0px;">
            <c:if test="${userName eq blog.writer}">
                <button class="btn btn-outline-danger col-1" type="button" onclick="location.href='/blog/delete?blogId=${blog.blogId}'">삭제</button>
                <form action="/blog/updateform" method="post" class="col">
                    <input type="hidden" name="blogId" value="${blog.blogId}">
                    <input type="submit" value="수정하기" class="btn btn-outline-info">
                </form>
            </c:if>
            <button class="btn btn-outline-secondary col-1" type="button" onclick="location.href='/blog/list'">목록</button>
        </div>
    </div><!-- .container -->

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
    <script>
        // 글 구성에 필요한 글 번호를 자바스크립트 변수에 저장
        let blogId = "${blog.blogId}";

        let str = "";

        // blogId를 받아 전체 데이터를 JS 내부로 가져오는 함수
        function getAllReplies(id){
            // <%-- jsp와 js가 모두 ${변수명} 문법을 공유하고, 이 중 .jsp파일에서는
            // ${}의 해석을 jsp 식으로 먼저 하기 때문에, 해당 ${}가 백틱 내부에서 쓰이는 경우
            // \${} 형식으로 \를 추가로 왼쪽에 붙여서 jsp용으로 작성한 것이 아님을 명시해야함. --%> jsp와 js 기준으로 둘다 주석처리를 해줘야함...
            let url = `http://localhost:8080/reply/\${id}/all`; 
            fetch(url, {method:'get'})
                .then((res) => res.json())
                .then(data => {
                    str = "";
                    // console.log(data);
                    // for(reply of data){
                    //     console.log(reply);
                    //     console.log("----------------------")
                    //     str += `<div class="row">
                    //                 <div class="col-8">
                    //                     \${reply.replyWriter}
                    //                 </div>
                    //                 <div class="col-4">
                    //                     \${reply.updateAt}
                    //                 </div>
                    //             </div>
                    //             <div class="row">
                    //                 \${reply.replyContent}
                    //             </div>`;

                    // .map을 이용한 간결한 반복문 처리
                    data.map((reply, i) => {
                        str += `<div class="row">
                                    <div class="col-10 no-padding">
                                        <span id="replyWriter\${reply.replyId}">\${reply.replyWriter}</span>
                                    </div>
                                    <div class="col-2">
                                        <span id="replyUpdateAt">\${reply.updateAt}</span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-10 no-padding">
                                        <span id="replyContent\${reply.replyId}">\${reply.replyContent}</span>
                                    </div>
                                    <div class="col-auto">
                                        <span class="deleteReplyBtn" data-replyId="\${reply.replyId}">[삭제]</span>
                                    </div>
                                    <dev class="col-auto">
                                        <span class="updateReplyBtn" data-bs-toggle="modal" data-bs-target="#replyUpdateModal" data-replyId="\${reply.replyId}">[수정]</span>
                                    </div>
                                </div>`;
                    });

                    const replies = document.getElementById("replies");
                    replies.innerHTML = str;
                });
        };

        getAllReplies(blogId);

        // 해당 함수 실행시 비동기 폼에 작성된 글쓴이, 내용으로 댓글 입력
        function insertReply() {
            let url = 'http://localhost:8080/reply';

            // 요소가 다 채워졌는지 확인
            if(document.getElementById("replyWriter").value.trim() === ''){
                alert("작성자를 채워주셔야 합니다.");
                return;
            }

            // 요소가 다 채워졌는지 확인
            if(document.getElementById("replyContent").value.trim() === ''){
                alert("내용을 채워주셔야 합니다.");
                return;
            }

            fetch(url, {
                method:'post',
                headers: {
                    // header에는 보내는 데이터의 자료형에 대해서 기술
                    // json 데이터를 요청과 함께 전달, @RequestBody를 입력받는 로직 추가
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    // 여기에 실질적으로 요청과 보낼 json 정보를 기술
                    replyWriter: document.getElementById("replyWriter").value,
                    replyContent: document.getElementById("replyContent").value,
                    blogId: "${blog.blogId}",
                }),  // insert 로직이기 때문에 response에 실제 화면에서 사용할 데이터 전송 X
            }).then(() => {
                // 댓글 작성 후 폼에 작성되어있던 내용 소거
                document.getElementById("replyWriter").value = "";
                document.getElementById("replyContent").value = "";
                alert("댓글 작성이 완료되었습니다.🥰");
                // getAllReplies("${blog.blogId}");
            }).then(() => {
                getAllReplies(blogId);
            });
        }

        // 제출 버튼에 이벤트 연결하기
        const $replySubmit = document.getElementById("replySubmit");
        $replySubmit.addEventListener("click", insertReply);

        // 이벤트 객체를 활용해야 이벤트 위임을 구현하기 수월하므로 먼저 html 객체부터 가져옵니다.
        // 모든 댓글을 포함하고 있으면서 가장 가까운 영역인 #replies에 설정
        const $replies = document.querySelector("#replies");
        $replies.onclick = (e) => {
            // 클릭한 요소가 #replies의 자식태그인 .deleteReplyBtn 인지 검사하기
            // 이벤트객체.target.lmtatches는 클릭한 요소가 어떤 태그인지 검사
            if(e.target.matches('#replies .deleteReplyBtn')){
                deleteReply();
            }else if(e.target.matches('#replies .updateReplyBtn')){
                openUpdateReplyModal();
            }else{
                return;
            }

            // 둘 다 가능 -- e.target.dataset.replyid
            

            function deleteReply() {
                const replyId = e.target.dataset['replyid'];

                if(!confirm("삭제하시겠습니까?")) return;

                let url = 'http://localhost:8080/reply/' + replyId;

                fetch(url, { method: 'delete' })
                    .then(() => {
                        alert("삭제 되었습니다.👻");
                        getAllReplies(blogId);
                    });
            }

            function openUpdateReplyModal() {
                const replyId = e.target.dataset['replyid'];
                document.getElementById("modalReplyId").value = replyId;

                let replyWriterId = `#replyWriter\${replyId}`;
                let replyContentId = `#replyContent\${replyId}`;

                document.getElementById("modalReplyWriter").value = document.querySelector(replyWriterId).textContent;
                document.getElementById("modalReplyContent").value = document.querySelector(replyContentId).textContent;
            }
        };

        $replyUpdateBtn = document.querySelector("#replyUpdateBtn");
        $replyUpdateBtn.onclick = (e) => {

            const replyId = document.getElementById("modalReplyId").value;
            const writer = document.getElementById("modalReplyWriter").value;
            const content = document.getElementById("modalReplyContent").value;

            let url = "http://localhost:8080/reply/" + replyId;

            fetch(url, {
                method: 'PATCH',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    replyWriter: writer,
                    replyContent: content,
                }),
            }).then(() => {
                document.getElementById("modalReplyWriter").value = "";
                document.getElementById("modalReplyContent").value = "";
                getAllReplies(blogId);
            });
        };

    </script>
</body>
</html>