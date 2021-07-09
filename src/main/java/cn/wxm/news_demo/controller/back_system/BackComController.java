package cn.wxm.news_demo.controller.back_system;

import cn.wxm.news_demo.domain.Article;
import cn.wxm.news_demo.domain.Comment;
import cn.wxm.news_demo.domain.User;
import cn.wxm.news_demo.service.CommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpSession;

/**
 * @author wxm
 * @date 2021-03-29 23:08
 * @Description 后台评论控制类
 */
@Slf4j
@Controller
@RequestMapping("/back/comment")
public class BackComController {
    @Autowired
    CommentService commentService;

    //新闻评论展示列表
    @RequestMapping("/comManage")
    public String commentManage(@RequestParam(value = "pn",defaultValue = "1")Integer pn,
                                @RequestParam(value = "uname",required = false)String uname,
                                @RequestParam(value = "lid",required = false)Integer lid,
                                @RequestParam( value = "illegal",required = false)Integer illegal,
                                Model model, HttpSession session){
        User back_user = (User) session.getAttribute("back_user");
        System.out.println(uname+":"+lid+":"+illegal);
        //默认第一页开始，一页20条
        Page<Comment> page = new Page<>(pn, 20);
        IPage<Comment> comPage = commentService.findComByStatus(page, lid, illegal, uname, back_user.getLid());
        //返回总页数
        long comTotalPage = comPage.getPages();

        model.addAttribute("comPage",comPage);
        model.addAttribute("comTotalPage",comTotalPage);
        return "/back/comment-manage";
    }

    //当评论过长，可查看评论详细页面
    @RequestMapping("/view")
    public String viewComment(Integer comid,Model model){
        Comment comment = commentService.getById(comid);
        model.addAttribute("comment",comment);
        return "/back/comment-view";
    }

    //新闻评论页面进行评论删除操作
    @PostMapping("/deleteComment")
    @ResponseBody
    public String deleteComment(Integer comid){
      commentService.removeById(comid);
        log.info("评论表主键comid：{}",comid);
        return "success";
    }
}
