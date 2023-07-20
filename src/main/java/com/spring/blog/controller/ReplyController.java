package com.spring.blog.controller;

import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import com.spring.blog.entity.Reply;
import com.spring.blog.exception.NotFoundReplyByReplyIdException;
import com.spring.blog.service.ReplyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/reply")
public class ReplyController {

    ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService){
        this.replyService = replyService;
    }

    // 글 번호에 맞는 전체 댓글을 가져오는 메서드
    // 어떤 자원에 접근할 것인지만 uri에 명시(메서드가 행동을 경정함)
    @RequestMapping(value = "/{blogId}/all", method = RequestMethod.GET)
    // rest서버는 응답시 응답코드와 응답객체를 넘기기 때문에 ResponseEctity<자료형>을 리턴
    public ResponseEntity<List<Reply>> findAllReplys(@PathVariable long blogId) {
        // 서비스에서 리플 목록을 들고 옵니다.
        List<Reply> findByIdDTO =  replyService.findAllByBlogId(blogId);
        return ResponseEntity
                .ok()   // 200 코드
                .body(findByIdDTO); // 리플 목록
    }

    @RequestMapping(value = "/{replyId}", method = RequestMethod.GET)
    public ResponseEntity<?> findByReplyId(@PathVariable long replyId){
        Reply findByIdDTO = replyService.findByReplyId(replyId);

        try {
            if(findByIdDTO == null){
                throw new NotFoundReplyByReplyIdException("업는 리플 번호를 조회 했습니다.");
            }
        }catch (NotFoundReplyByReplyIdException e){
            e.printStackTrace();
            return new ResponseEntity<>("찾는 댓글 번호가 없습니다.", HttpStatus.NOT_FOUND);
        }
        
        // 다양한 방법으로 사용 가능
//        return new ResponseEntity<ReplyFindByIdDTO>(findByIdDTO, HttpStatus.OK);
        return ResponseEntity.ok(findByIdDTO);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    // Rest 컨트롤러는 데이터를 json으로 주고 받기 때문에
    // @RequestBody 어노테이션을 이용하여 역직렬화를 하도록 설정
    public ResponseEntity<?> insertReply(@RequestBody Reply reply){
        replyService.save(reply);
        return ResponseEntity.ok("댓글 등록이 잘 되었습니다.");
    }

    @RequestMapping(value = "/{replyId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteReply(@PathVariable long replyId){
        replyService.deleteByReplyId(replyId);
        return ResponseEntity.ok("댓글이 정상적으로 삭제되었습니다.");
    }

    @RequestMapping(value = "/{replyId}", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<?> updateReply(@PathVariable long replyId, @RequestBody Reply dto){

        log.info("replyId 주입 전 : " + dto);
        dto.setReplyId(replyId);
        log.info("replyId 주입 후 : " + dto);

        replyService.update(dto);
        return ResponseEntity.ok("댓글이 정상적으로 변경되었습니다.");
    }

}
