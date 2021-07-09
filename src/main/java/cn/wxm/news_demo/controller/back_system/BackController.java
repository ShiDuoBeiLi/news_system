package cn.wxm.news_demo.controller.back_system;

import cn.wxm.news_demo.domain.Category;
import cn.wxm.news_demo.service.CateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: news_demo
 * @description: 后台管理系统首页
 * @author: wxm
 * @create: 2021-02-28 15:46
 **/
@RequestMapping("/back")
@Controller
@Slf4j
public class BackController {
    @Autowired
    CateService cateService;

    //后台系统登录页
    @RequestMapping("/login")
    public String backLogin(){
        return "/back/login";
    }

    //后台系统首页
    @RequestMapping("/index")
    public String backIndex(){
        return "/back/index";
    }

    //欢迎页
    @RequestMapping("/welcome")
    public String backWelcome(){
        return "/back/welcome";
    }



}
