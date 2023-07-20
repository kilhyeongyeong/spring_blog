package com.spring.blog.controller;

import com.spring.blog.entity.Blog;
import com.spring.blog.exception.NotFoundBlogException;
import com.spring.blog.service.BlogService;
import com.spring.blog.service.BlogServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller // 컨트롤러 어노테이션의 기능 1. 빈등록, 2. URL 매핑 처리 기능
            // 2개의 기능을 갖고 있으므로 다른 어노테이션과 교환해서 사용할 수 없다
@RequestMapping("/blog")
@Log4j2     // sout이 아닌 로깅을 통한 디버깅을 위해 선언
public class BlogController {

    BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }

    @GetMapping(value = {"/list", "/list/page={page}"})
    public String findAll(Model model, @PathVariable(name = "page", required = false)Long page){
        Page<Blog> pageInfo = blogService.findAll(page);

        // 한 페이지에 보여야 하는 페이징 버튼 그룹의 개수
        final int PAGE_BTN_NUM = 10;

        // 현재 조회중인 페이지 번호(0부터 세므로 주의)
        int currentPageNum = pageInfo.getNumber() + 1;

        // 현재 조회중인 페이지 그룹의 끝번호
        int endPageNum = (int)Math.ceil((double)currentPageNum / PAGE_BTN_NUM) * PAGE_BTN_NUM;

        // 현재 조회중인 페이지 시작 번호
        int startPageNum = endPageNum - PAGE_BTN_NUM + 1;

        // 마지막 그룹 번호 보정
        endPageNum = endPageNum > pageInfo.getTotalPages() ? pageInfo.getTotalPages() : endPageNum;

        model.addAttribute("currentPageNum", currentPageNum);
        model.addAttribute("endPageNum", endPageNum);
        model.addAttribute("startPageNum", startPageNum);
        model.addAttribute("pageInfo", pageInfo);
        return "/board/list";
    }

    @GetMapping("/detail/{blogId}")
    public String detail(@PathVariable(name = "blogId") long blogId, Model model, Principal principal){
        model.addAttribute("userName", principal.getName());
        Blog blog = blogService.findById(blogId);

        if (blog == null){
            try{
                throw new NotFoundBlogException("없는 blogId로 조회했습니다. 조회 번호 : " + blogId);
            }catch (NotFoundBlogException e){
                e.printStackTrace();
                return "blog/NotFoundBlogIdExceptionResultPage";
            }
        }

        model.addAttribute("blog", blog);
        return "/blog/detail";
    }

    // 폼 페이지와 실제 등록 url은 같은 url을 쓰도록 합니다.
    // 대신 폼 페이지는 GET방식으로 접속했을 때 연결해주고
    // 폼에서 작성완료한 내뇬을 POST방식으로 제출해 저장하도록 만들어 줍니다.
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public String insert(Model model, Principal principal){

        // SecurityContext, Principal 은 둘 다 인증정보를 가지고 있는 객체 - 둘 중 원하는 것 사용
        // principal.getName() : 현재 로그인 유저의 아이디 리턴
        model.addAttribute("userName", principal.getName()); 

        return "/blog/blog-form";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(Blog blog){
        blogService.save(blog);
        return "redirect:/blog/list";
    }
    
    // Delete 로직은 삭제 후 /blog/lsit로 리다이렉트 되어서 자료가 삭제된 것을 확인해야 합니다.
    // 글 번호만으로 삭제를 진행해야 합니다.
    // 따라서, 디테일 페이지에 삭제버튼을 추가하고, 해당 버튼을 클릭했을 때, 삭제 번호가 전달되어서
    // 전달받은 번호를 토대로 삭제하도록 로직을 구성해주시면 됩니다.
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(long blogId){
        log.info(blogId);
        blogService.deleteById(blogId);
        return "redirect:/blog/list";
    }

    // Update 구문은 다른 내역은 다 insert와 비슷하지만
    // 한 가지 차이점은, 폼이 이미 기존에 작성된 정보로 채워져 있다는 점 입니다.
    // 이를 구현하기 위해 수정 버튼이 눌렸을 때, 제일 먼저 해당 글 정보를 획득한 다음
    // 폼 페이지에 model.addtribute()로 보내줘야 합니다.
    // 그 다음 수정용 폼 페이지에 해당 자료를 채운 채 연결해주면 됩니다.
    // 이를 위해 value= 를 이용하면 미리 원하는 내용으로 폼을 채울 수 있습니다.
    @RequestMapping(value = "/updateform", method = RequestMethod.POST)
    public String updateform(long blogId, Model model){
        log.info(blogId);
        model.addAttribute("blog", blogService.findById(blogId));
        return "/blog/blog-update-form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Blog blog){
        blogService.update(blog);
        return "redirect:/blog/detail/" + blog.getBlogId();
    }
}
