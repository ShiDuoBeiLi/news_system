package cn.wxm.news_demo.service.impl;

import cn.wxm.news_demo.domain.*;
import cn.wxm.news_demo.mapper.ArticleMapper;
import cn.wxm.news_demo.mapper.CommentMapper;
import cn.wxm.news_demo.mapper.FavoriteMapper;
import cn.wxm.news_demo.mapper.UserMapper;
import cn.wxm.news_demo.service.ArticleService;
import cn.wxm.news_demo.service.CommentService;
import cn.wxm.news_demo.utils.ImageUtil;
import cn.wxm.news_demo.utils.SensitivewordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sun.mail.imap.protocol.ID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: demo
 * @description:
 * @author: wxm
 * @create: 2021-02-17 13:52
 **/
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    FavoriteMapper favoriteMapper;

    @Autowired
    CommentMapper commentMapper;
    
    @Autowired
    UserMapper userMapper;

    //保存文章,其中状态码data为200成功，400用户未激活账号发表失败
    @Override
    public Map savaArticle(Article article, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        User user =(User) session.getAttribute("user");
//        log.info("{}",article);
//        判断是修改新闻还是保存
        if (article.getId()!=null){
            //进行新闻的修改
            Article oriArt = articleMapper.selectOne(new QueryWrapper<Article>().eq("id", article.getId()));
            article.setLid(oriArt.getLid());
            article.setCheckNum(oriArt.getCheckNum());
            article.setCreateTime(oriArt.getCreateTime());
            article.setEditTime(new Date());
            //标题过滤
            article.setTitle(SensitivewordUtil.replaceSensitiveWord(article.getTitle(),1,"*"));
            //保持新闻原来审核状态
            article.setStatus(oriArt.getStatus());
            article.setAuthorId(user.getUid());
            String content = article.getContent();
            //进行内容过滤,最小匹配规则
            content = SensitivewordUtil.replaceSensitiveWord(content, 1, "*");
            //设置文章图片
            List<String> imgStr = ImageUtil.getImgStr(content);
            if (imgStr==null||imgStr.size()==0){

                article.setImgSrc("/images/white.jpg");
            }else {
                //取第一张图片即可
                String src = imgStr.get(0);
                article.setImgSrc(src);
            }
            articleMapper.updateById(article);
            map.put("data",201);
        }else {
            //是新闻添加
            //设置文章图片
            String content = article.getContent();
            //进行内容和标题过滤
            content=SensitivewordUtil.replaceSensitiveWord(content,1,"*");
            article.setTitle(SensitivewordUtil.replaceSensitiveWord(article.getTitle(),1,"*"));
            article.setContent(content);
            List<String> imgStr = ImageUtil.getImgStr(content);
            if (imgStr==null||imgStr.size()==0){

                article.setImgSrc("/images/white.jpg");
            }else {
                //取第一张图片即可
                String src = imgStr.get(0);
                article.setImgSrc(src);
            }
            //设置用户名uid
//            User user =(User) session.getAttribute("user");
            //有登录并且用户字段active为1
            if (user!=null&&user.getActive()==1){
                //最高等级用户默认文章不用审核
                if (user.getLid()==3){
                    article.setStatus(1);
                }
                article.setAuthorId(user.getUid());
                article.setCreateTime(new Date());
                article.setLid(user.getLid());
                articleMapper.insert(article);
                map.put("data",200);
            }else
            {
                //用户未激活账号不能发表文章
                map.put("data",400);
            }
        }


       return map;
    }

    @Override
    public List<Article> addCateAll( ) {
        //先查询完整字段,进行分类查询
        List<Article> articles = articleMapper.addCateAll();
        return articles;
    }

    //首页显示数据
    @Override
    public IPage<Article> findByPage(Integer current, Integer size,HttpSession session) throws Exception {
        IPage<Article> articlePage = new Page<>(current,size);
        IPage<Article> page = null;
        User user = (User) session.getAttribute("user");

        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        //根据等级，审核状态罗列首页数据
        if (user!=null){
            queryWrapper.eq("t_article.lid",user.getLid()).eq("t_article.status",1).orderByDesc("create_time");
        }else {
            //没有登录显示的为最低等级
            queryWrapper.eq("t_article.lid",1).eq("t_article.status",1).orderByDesc("create_time");
        }
//        queryWrapper.orderByDesc("create_time");
        try {
            page=this.articleMapper.findByPage(articlePage,queryWrapper);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }

        return page;
    }

    //返回新闻点击详情
    @Override
    public Article findMessageId(Integer aid) {
        Article messageId = articleMapper.findMessageId(aid);
        messageId.setCheckNum(messageId.getCheckNum()+1);
        articleMapper.updateById(messageId);
        return messageId;
    }


    //能够根据搜索条件查找相关新闻信息返回给后台管理系统，其中文章等级小于用户本身等级
    @Override
    public IPage<Article> findAllByStatus(IPage<Article> page, String uname, String cname, String title, HttpSession session
    ,Integer status) {
        User back_user = (User) session.getAttribute("back_user");
        IPage<Article> pageStatus = articleMapper.findAllByStatus(page, uname, cname, title, back_user.getLid(),status);
        return pageStatus;
    }

    //新闻收藏
    @Override
    public Map artFavorite(Integer id, HttpSession session) {
        Favorite fav = new Favorite();
        User user = (User) session.getAttribute("user");
        HashMap<String, Object> map = new HashMap<>();
        Integer flag;
        String msg;
        //用户登录
        if (user!=null){
            //用户激活
            if (user.getActive()==1){
                //判断是否重复收藏
                QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
                wrapper.eq("aid",id);
                wrapper.eq("uid",user.getUid());
                Favorite favorite = favoriteMapper.selectOne(wrapper);
                if (favorite!=null){
                    flag=401;
                    msg="新闻已收藏，请勿重复收藏";
                }else {
                    //当用户尚未收藏过该新闻
                    fav.setAid(id);
                    fav.setUid(user.getUid());
                    fav.setAddTime(new Date());
                    favoriteMapper.insert(fav);
                    flag=200;
                    msg="收藏成功";
                }
            }else {
                flag=402;
                msg="请激活后再进行新闻收藏";
            }
//            //判断是否重复收藏
//            QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
//            wrapper.eq("aid",id);
//            wrapper.eq("uid",user.getUid());
//            Favorite favorite = favoriteMapper.selectOne(wrapper);
//            if (favorite!=null){
//                flag=401;
//                msg="新闻已收藏，请勿重复收藏";
//            }else {
//                //当用户尚未收藏过该新闻
//                fav.setAid(id);
//                fav.setUid(user.getUid());
//                fav.setAddTime(new Date());
//                favoriteMapper.insert(fav);
//                flag=200;
//                msg="收藏成功";
//            }

        }else{
            flag=400;
            msg="用户尚未登录，请登陆后再收藏新闻";
        }
        map.put("flag",flag);
        map.put("msg",msg);
        return map;
    }

    //设置新闻排行榜数据
    @Override
    public void getRowData(HttpSession session) {
        User user = (User) session.getAttribute("user");
        //新闻点击榜查询
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        //获取最新发布的新闻
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        //点击排行榜前6条
        if (user==null){
            //用户未登录
            wrapper.eq("lid",1).eq("status",1).orderByDesc("check_num").last("limit 6");
            queryWrapper.eq("lid",1).eq("status",1).orderByDesc("create_time").last("limit 6");
        }else {
            wrapper.eq("lid",user.getLid()).eq("status",1).orderByDesc("check_num").last("limit 6");
            queryWrapper.eq("lid",user.getLid()).eq("status",1).orderByDesc("create_time").last("limit 6");
        }
        //存到session
        List<Article> articles = articleMapper.selectList(wrapper);
        List<Article> newList = articleMapper.selectList(queryWrapper);
        session.setAttribute("rowLists",articles);
        session.setAttribute("newLists",newList);

    }


    //获取最新评论的新闻
    @Override
    public List<Article> getNewComArt(HttpSession session) {
        User user =(User) session.getAttribute("user");
        Integer lid=1;
        if (user!=null){
            lid=user.getLid();
        }else {
            lid=1;
        }
        //获取所有用户对应等级的uid
//        QueryWrapper<User> userWrapper = new QueryWrapper<>();
//        List<User> userList = userMapper.selectList(userWrapper.eq("lid", lid));
//        List<Integer> userIds = userList.stream().map(User::getUid).collect(Collectors.toList());
        //需要根据相应等级获取对应新闻的id
        QueryWrapper<Article> artWrapper = new QueryWrapper<>();
        List<Article> articles1 = articleMapper.selectList(artWrapper.eq("lid", lid));
        List<Integer> artList = articles1.stream().map(Article::getId).collect(Collectors.toList());

        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        //distinct去除重复aid，限制六条，还需要判断符合相关等级新闻
        wrapper.select("distinct aid").in("aid",artList).orderByDesc("com_time").last("limit 6");
        List<Comment> comments = commentMapper.selectList(wrapper);
        //获取相应的新闻aid
        List<Integer> aids = comments.stream().map(Comment::getAid).collect(Collectors.toList());
        List<Article> articles = articleMapper.selectBatchIds(aids);
        return articles;
    }

    //进行新闻搜素
    @Override
    public List<Article> searchArt(HttpSession session, String keyword) {
        User user =(User) session.getAttribute("user");
        Integer lid=1;
        //搜素相关等级的新闻
        if (user!=null){
            lid=user.getLid();
        }else {
            lid=1;
        }
        QueryWrapper<Article> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("lid",lid);
        //需要防止sql
        wrapper1.and(wrapper->wrapper.like("title",keyword).or().like("content",keyword));
        List<Article> articles = articleMapper.selectList(wrapper1);
        return articles;
    }

    //其他等级用户查询信息
    @Override
    public IPage<Article> findOtherLevPage(Integer pageNum, Integer pageSize, Integer cid, Integer level, String keyword) throws Exception {
        System.out.println(pageNum+":"+pageSize+":"+cid+":"+level+":"+keyword);
        IPage<Article> articlePage = new Page<>(pageNum,pageSize);
        IPage<Article> page = null;
        QueryWrapper<Article> wrapper1 = new QueryWrapper<>();
        //查询条件，需要审核通过
        if (cid!=null){
            wrapper1.eq("t_article.cid",cid);
        }
        wrapper1.eq("t_article.lid",level).eq("t_article.status",1).orderByDesc("create_time");
        //条件筛选
        if (StringUtils.isNotBlank(keyword)){
            wrapper1.and(wrapper ->wrapper.like("title",keyword).or().like("content",keyword));
        }

        try {
            page=this.articleMapper.findByPage(articlePage,wrapper1);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }

        return page;
    }


}
