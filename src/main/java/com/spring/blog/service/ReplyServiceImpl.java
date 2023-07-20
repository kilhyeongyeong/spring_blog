package com.spring.blog.service;

import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import com.spring.blog.entity.Reply;
import com.spring.blog.repository.ReplyJPARepository;
import com.spring.blog.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService{

    ReplyRepository replyRepository;

    ReplyJPARepository replyJPARepository;

    @Autowired
    public ReplyServiceImpl(ReplyRepository repository, ReplyJPARepository replyJPARepository){
        this.replyRepository = repository;
        this.replyJPARepository = replyJPARepository;
    }

    @Override
    public List<Reply> findAllByBlogId(long blogId) {
//        return replyRepository.findAllByBlogId(blogId);
        return replyJPARepository.findAllByBlogId(blogId);
    }

    @Override
    public Reply findByReplyId(long replyId) {
//        return replyRepository.findByReplyId(replyId);
        return replyJPARepository.findById(replyId).get();
    }

    @Override
    public void deleteByReplyId(long replyId) {
//        replyRepository.deleteByRelplyId(replyId);
        replyJPARepository.deleteById(replyId);
    }

    @Override
    public void deleteByBlogId(long blogId) {
        replyJPARepository.deleteByBlogId(blogId);
    }


    @Override
    public void save(Reply dto) {
        replyJPARepository.save(dto);
    }

    @Override
    public void update(Reply reply) {
        Reply replyUpdate = replyJPARepository.findById(reply.getReplyId()).get();

        replyUpdate.setReplyContent(reply.getReplyContent());

        replyJPARepository.save(replyUpdate);
    }
}
