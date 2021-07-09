package cn.wxm.news_demo.controller;

import cn.wxm.news_demo.domain.Comment;
import cn.wxm.news_demo.service.CommentService;
import cn.wxm.news_demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: news_demo
 * @description: 评论操作
 * @author: wxm
 * @create: 2021-03-16 22:38
 **/

@Controller
@Slf4j
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    //用户添加评论
    @PostMapping("/addCom")
    @ResponseBody
    public Map addComment(@RequestBody Comment comment, HttpSession session){
//        log.info("{}",comment);
        Map map = commentService.addComment(comment, session);
        return map;

    }
}
