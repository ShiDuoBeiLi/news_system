package cn.lyl.news_system.service.impl;

import cn.lyl.news_system.domain.Article;
import cn.lyl.news_system.domain.Favorite;
import cn.lyl.news_system.domain.User;
import cn.lyl.news_system.mapper.ArticleMapper;
import cn.lyl.news_system.mapper.FavoriteMapper;
import cn.lyl.news_system.mapper.UserMapper;
import cn.lyl.news_system.service.UserService;
import cn.lyl.news_system.utils.MD5Util;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: news_system
 * @description:
 * @author: lyl
 *
 **/

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    FavoriteMapper favoriteMapper;


    //返回注册结果信息
    @Override
    public Map registerResult(Map<String,String> map, HttpSession session) {
        String checkcode_server = (String)session.getAttribute("CHECKCODE_SERVER");
        User user = new User();
        user.setUname(map.get("uname"));
        user.setPassword(map.get("password"));
        user.setSex(map.get("sex"));
        user.setEmail(map.get("email"));
        user.setTelephone(map.get("telephone"));
        user.setNewsName(map.get("news_name"));
        String checkcode=map.get("checkcode");
        Map<String, String> map1 = new HashMap<>();
        //存放返回码状态和查询信息结果
        Integer status;
        String msg="";
        if (checkcode.equalsIgnoreCase(checkcode_server)){
//            log.info("验证码阶段{}");
            msg=null;
            //验证码一致
            status=200;
            //进行用户保存
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uname",user.getUname());
            User user1 = userMapper.selectOne(queryWrapper);
            //用户名没有重复
            if (user1 == null) {
                //使用MD5进行加密密码后保存
                user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
                //设置用户头像为默认图片
                user.setHeadImage("/images/none.jpg");
                user.setCreateTime(new Date());
                userMapper.insert(user);
                msg = "注册成功";
            } else {
                status = 401;
                msg = "用户名已存在，请选择 新的用户名";
            }

        }else{
//            log.info("这里执行2");
            status=400;
            msg="验证码输入错误，请重新输入";
        }

        map.put("msg",msg);
        map.put("status",status.toString());
        System.out.println(map.toString());
        return map;
    }

    //返回登录结果信息
    @Override
    public Map loginResult(Map<String, String> map, HttpSession session) {
        //数据取出与查询
        String uname=map.get("uname");
        String password=MD5Util.MD5EncodeUtf8(map.get("password"));
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uname",uname).eq("password",password);
        User user = userMapper.selectOne(wrapper);
        String checkcode=map.get("checkcode");
        //取出验证码
        String checkcode_server = (String)session.getAttribute("CHECKCODE_SERVER");
        HashMap<String, Object> map1 = new HashMap<>();
        //存放返回码状态和查询信息结果
        Integer status;
        String msg="";

        if (checkcode.equalsIgnoreCase(checkcode_server)){
            msg=null;
            //验证码一致
            //查询数据库是否有用户账户信息
            if (user==null){
                //查询不到，重新登录
                msg="用户名或密码输入错误，请重新输入";
                status=401;
            }else {
                //用户输入正确
                status=200;
                msg="登录成功，即将返回首页";
                //将用户数据保存到session中
                session.setAttribute("user",user);
            }

        }else{
//            log.info("这里执行2");
            status=400;
            msg="验证码输入错误，请重新输入";
        }
        map1.put("msg",msg);
        map1.put("status",status.toString());
        return map1;
    }

    //返回登录后端查询结果
    @Override
    public Map backLoginResult(Map<String, String> map, HttpSession session) {
        //数据取出与查询
        String uname=map.get("uname");
        String password=MD5Util.MD5EncodeUtf8(map.get("password"));
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uname",uname).eq("password",password);
        User back_user = userMapper.selectOne(wrapper);
        String captcha=map.get("captcha");
        //取出验证码
        String checkcode_server = (String)session.getAttribute("CHECKCODE_SERVER");
        HashMap<String, Object> map1 = new HashMap<>();
        //存放返回码状态和查询信息结果
        Integer status;
        String msg="";
        if (captcha.equals(checkcode_server)) {

            if (back_user != null) {
                //判断用户级别和是否管理员
                if (back_user.getLid()<2||back_user.getAdRole()==0){
                    status=401;
                    msg="该用户权限不够，不能进入后台管理系统";
                } else {
                    status=200;
                    msg="登录成功，即将进入后台管理系统";
                    session.setAttribute("back_user",back_user);
                }

            }else {
                status=400;
                msg="用户名或密码错误，请重检查是否输入有误";
            }

        }else {
            status=402;
            msg="验证码不正确，请重新输入验证码";
        }

        map1.put("status",status);
        map1.put("msg",msg);
        return map1;
    }

    //返回用户收藏的文章
    @Override
    public List<Article> getFavArt(Integer uid) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        QueryWrapper<Favorite> favWrappery = new QueryWrapper<>();
        //根据用户id获取收藏的所有新闻
        List<Favorite> fav_aid = favoriteMapper.selectList(favWrappery.eq("uid",uid));
        //谋取列表中单一字段aid的list集合
        List<Integer> collectAid=new ArrayList<>();
        List<Article> articles=new ArrayList<>();
        //用户有收藏才执行
        if (fav_aid!=null&&!fav_aid.isEmpty()){
            //取出所有aid
            collectAid= fav_aid.stream().map(Favorite::getAid).collect(Collectors.toList());
            articles = articleMapper.selectBatchIds(collectAid);
        }
        return articles;
    }

    //修改用户密码
    @Override
    public Map alterPass(Map<String, String> map, HttpSession session) {
        Map<String, Object> res_map = new HashMap<>();
        String cur_pass = map.get("cur_pass");
        String password = map.get("password");
        User user =(User) session.getAttribute("user");
//        log.info("session的信息{}",user);
        //密码相等
        if (MD5Util.MD5EncodeUtf8(cur_pass).equalsIgnoreCase(user.getPassword())){
            //加密后保存，更新session
            user.setPassword(MD5Util.MD5EncodeUtf8(password));
            session.setAttribute("user",user);
            //更新数据库
            int i = userMapper.updateById(user);
            log.info("{}",i);
            res_map.put("flag",200);
        }else {
            //不相等
            res_map.put("flag",400);
        }
        return res_map;
    }
}
