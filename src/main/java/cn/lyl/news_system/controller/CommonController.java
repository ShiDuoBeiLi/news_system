package cn.lyl.news_system.controller;

import cn.lyl.news_system.domain.Category;
import cn.lyl.news_system.service.CateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @program: news_system
 * @description: 页面公共类管理
 * @author: lyl
 *
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
