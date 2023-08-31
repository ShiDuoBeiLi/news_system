package cn.lyl.news_system.controller;

import cn.lyl.news_system.domain.Comment;
import cn.lyl.news_system.service.CommentService;
import cn.lyl.news_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @program: news_system
 * @description: 评论操作
 * @author: lyl
 *
 **/

@Controller
@Slf4j
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    UserService userServiced;

    //用户添加评论
    @PostMapping("/addCom")
    @ResponseBody
    public Map addComment(@RequestBody Comment comment, HttpSession session){
        log.info("{}",comment);
        System.out.println(comment);
        System.out.println(session);
        Map map = commentService.addComment(comment, session);
        return map;

    }
}
