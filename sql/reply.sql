# 댓글 테이블 생성 구문
CREATE TABLE reply(
	reply_id INT PRIMARY KEY AUTO_INCREMENT,
    blog_id INT NOT NULL,
    reply_writer VARCHAR(40) NOT NULL,
    reply_content VARCHAR(200) NOT NULL,
    published_at DATETIME DEFAULT NOW(),
    update_at DATETIME DEFAULT NOW()
);

# 외래키 설정
# blog_id에는 기존에 존재하는 글의 blog_id 만 들어가야 한다.
ALTER TABLE reply ADD CONSTRAINT fk_reply FOREIGN KEY (blog_id) REFERENCES blog(blog_id);

# 더미 데이터 입력 - 테스트 DB에서만 사용
INSERT INTO reply VALUES(NULL, 2, "댓글쓴사람", "1빠 댓글", NULL, NULL),
						(NULL, 2, "하이", "헬룽 안녀어엉:)", NULL, NULL),
                        (NULL, 2, "헬로", "나이스튜미튜!", NULL, NULL),
                        (NULL, 2, "안녕", "잘가~~", NULL, NULL),
                        (NULL, 3, "바이", "바바이><", NULL, NULL);