package cn.wxm.news_demo.service.impl;

import cn.wxm.news_demo.domain.Category;
import cn.wxm.news_demo.domain.Comment;
import cn.wxm.news_demo.domain.Favorite;
import cn.wxm.news_demo.domain.User;
import cn.wxm.news_demo.mapper.CateMapper;
import cn.wxm.news_demo.mapper.CommentMapper;
import cn.wxm.news_demo.mapper.UserMapper;
import cn.wxm.news_demo.service.CateService;
import cn.wxm.news_demo.service.CommentService;
import cn.wxm.news_demo.utils.SensitivewordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.dialect.AbstractDialect;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: news_demo
 * @description:处理评论
 * @author: wxm
 * @create: 2021-02-19 16:03
 **/

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    UserMapper userMapper;

    //用户添加评论操作
    @Override
    public Map addComment(Comment comment, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();
        User user = (User) session.getAttribute("user");
        //是否有登录，有才能评论
        if (user!=null){
//            判断是否有激活账号
            if (user.getActive()==1){
                //查看是否包含敏感词,最小匹配
                boolean isIllegal = SensitivewordUtil.isContaintSensitiveWord(comment.getContent(), 1);
                if (isIllegal){
                    //有进行评论过滤
                    comment.setIllegal(1);
                    comment.setContent(SensitivewordUtil.replaceSensitiveWord(comment.getContent(),1,"*"));
                }else{
                    comment.setIllegal(0);
                }
                comment.setUid(user.getUid());
                comment.setComTime(new Date());
                commentMapper.insert(comment);
                //标志位，表示成功
                map.put("flag",200);
            }else {
//                未激活
                map.put("flag",401);
            }

        }else {
//            未登录
            map.put("flag",400);
        }
        return map;
    }

    //获取相关评论的用户信息
    @Override
    public List<User> getComUser(Integer aid) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        List<Comment> com = commentMapper.selectList(wrapper.eq("aid", aid));
        List<Integer> userIds = new ArrayList<>();
        List<User> users = new ArrayList<>();
        //是否有评论
        if (com!=null&&!com.isEmpty()){
            userIds=com.stream().map(Comment::getUid).collect(Collectors.toList());
            users= userMapper.selectBatchIds(userIds);
        }
        return users;
    }

    @Override
    public IPage<Comment> findComByStatus(IPage<Comment> page, Integer lid, Integer illegal, String uname, Integer userLid) {
        IPage<Comment> comPage = commentMapper.findComByStatus(page, lid, illegal, uname, userLid);
        return comPage;
    }


}
