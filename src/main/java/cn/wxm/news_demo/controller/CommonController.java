package cn.wxm.news_demo.controller;

import cn.wxm.news_demo.domain.Category;
import cn.wxm.news_demo.service.CateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @program: news_demo
 * @description: 页面公共类管理
 * @author: wxm
 * @create: 2021-02-20 11:38
 **/

@Controller
@RequestMapping("/common")
public class CommonController {
    @Autowired
    CateService cateService;

    @RequestMapping("/getCategory")
    @ResponseBody
    public List<Category> getCategory(Model model){
        List<Category> cateList = cateService.list();
        model.addAttribute("cateList",cateList);
        return cateList;
    }

//    @GetMapping("/common/CateArticle/{cid}")
//    public String CateArticle(@PathVariable("cid")Integer cid, Model model){
//
//        return "article";
//    }
}
