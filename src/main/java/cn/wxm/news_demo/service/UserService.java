package cn.wxm.news_demo.service;

import cn.wxm.news_demo.domain.Article;
import cn.wxm.news_demo.domain.Category;
import cn.wxm.news_demo.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    public Map registerResult(Map<String,String> map, HttpSession session);
    public Map loginResult(Map<String,String> map, HttpSession session);

    public User activeEmail(User user);

    String activeResult(String code,HttpSession session);

    public Map backLoginResult(Map<String, String> map, HttpSession session);

    List<Article> getFavArt(Integer uid);

    Map alterPass(Map<String,String> map,HttpSession session);
}
