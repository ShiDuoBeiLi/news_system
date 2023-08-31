package cn.lyl.news_system.service;

import cn.lyl.news_system.domain.Article;
import cn.lyl.news_system.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    public Map registerResult(Map<String,String> map, HttpSession session);
    public Map loginResult(Map<String,String> map, HttpSession session);

    public Map backLoginResult(Map<String, String> map, HttpSession session);

    List<Article> getFavArt(Integer uid);

    Map alterPass(Map<String,String> map,HttpSession session);
}
