package cn.lyl.news_system.controller.back_system;

import cn.lyl.news_system.service.CateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: news_system
 * @description: 后台管理系统首页
 * @author: lyl
 *
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
        return "back/login";
    }

    //后台系统首页
    @RequestMapping("/index")
    public String backIndex(){
        return "back/index";
    }

    //欢迎页
    @RequestMapping("/welcome")
    public String backWelcome(){
        return "back/welcome";
    }



}
