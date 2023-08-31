package cn.lyl.news_system.service.impl;

import cn.lyl.news_system.domain.Comment;
import cn.lyl.news_system.mapper.CommentMapper;
import cn.lyl.news_system.mapper.UserMapper;
import cn.lyl.news_system.service.CommentService;
import cn.lyl.news_system.utils.SensitivewordUtil;
import cn.lyl.news_system.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: news_system
 * @description:处理评论
 * @author: lyl
 *
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
        System.out.println(user);
        //是否有登录，有才能评论
        if (user!=null){
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
