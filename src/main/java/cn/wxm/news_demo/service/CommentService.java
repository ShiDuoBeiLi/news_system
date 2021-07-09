package cn.wxm.news_demo.service;

import cn.wxm.news_demo.domain.Category;
import cn.wxm.news_demo.domain.Comment;
import cn.wxm.news_demo.domain.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface CommentService extends IService<Comment> {
    public Map addComment(Comment comment, HttpSession session);

    public List<User> getComUser(Integer aid);
//后台评论管理，可根据条件查询
    IPage<Comment> findComByStatus(IPage<Comment> page,Integer lid, Integer illegal, String uname, Integer userLid);

}
