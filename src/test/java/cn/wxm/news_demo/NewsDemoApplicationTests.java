package cn.wxm.news_demo;

import cn.wxm.news_demo.domain.*;
import cn.wxm.news_demo.mapper.ArticleMapper;
import cn.wxm.news_demo.mapper.CateMapper;
import cn.wxm.news_demo.mapper.FavoriteMapper;
import cn.wxm.news_demo.service.*;
import cn.wxm.news_demo.utils.SensitivewordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@SpringBootTest
class NewsDemoApplicationTests {
    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    CateService cateService;

    @Autowired
    CateMapper cateMapper;

    @Autowired
    ArtImageService artImageService;

    @Autowired
    ArticleService articleService;
    @Autowired
    UserService userService;

    @Autowired
    FavoriteMapper favoriteMapper;

    @Autowired
    CommentService commentService;

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() {

//        查询
//        Article article1 = new Article();
//        article1=    articleMapper.selectById(18);

//        Category category = new Category();
//        category.setCname("hh");
//        boolean save = cateService.save(category);
//        System.out.println(category);

//        List<Article> articles = articleMapper.selectList(null);
//        System.out.println(articles);

        Article article = articleMapper.selectById(17);
        UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("img_src","hh");
        articleMapper.update(article,updateWrapper);
        article = articleMapper.selectById(17);
        System.out.println(article);
    }
    @Test
    void test(){
        //测试写的解析富文本图片方法
        Article article1 = articleMapper.selectById(28);
        artImageService.saveArtImage(article1);
        List<ArtImage> list = artImageService.list();
        System.out.println(list);
    }
    @Test
    void cateArtTest(){

        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid",2);
        List<Article> list1 = articleService.list(queryWrapper);
        System.out.println(list1.size());
    }

    @Test
    void addCateAllTest()
    {
        List<Article> articles = articleService.addCateAll();
        System.out.println(articles);
    }

    @Test
    void testArticleVo() throws Exception {
//        ArticleVo articleVo = new ArticleVo();
//        System.out.println(articleVo
//        );
//        IPage<Article> byPage = articleService.findByPage(1, 5);
//        List<Article> records = byPage.getRecords();
//        System.out.println(records);
    }

    @Test
    void testArticleStatu(){
//        IPage<Article> allByStatus = articleMapper.findAllByStatus(null, null, "特朗普", 2);
//        System.out.println(allByStatus);
        IPage<Article> page = new Page<>(1,2);
        IPage<Article> allByStatus = articleMapper.findAllByStatus(page, null, null, null, 2,0);
        List<Article> records = allByStatus.getRecords();
        long total = allByStatus.getTotal();
        System.out.println(total);
    }

    @Test
    void testUser(){
        User byId = userService.getById(9);
//        System.out.println(byId);
    }
    //测试单一字段
    @Test
    void testOne(){
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        List<Favorite> uids = favoriteMapper.selectList(wrapper.eq("uid", 17));
        //获取某列表的某字段集合
        List<Integer> collect = uids.stream().map(Favorite::getAid).collect(Collectors.toList());
        List<Article> articles = articleMapper.selectBatchIds(collect);
        System.out.println(articles);
//        System.out.println(collect);
    }

    @Test
    void testSensitive(){
        System.out.println("敏感词的数量：" + SensitivewordUtil.sensitiveWordMap.size());
        String string = "太多的伤感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
                + "然后法轮功 我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
                + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。";
        System.out.println("待检测语句字数：" + string.length());
        long beginTime = System.currentTimeMillis();
        Set<String> set = SensitivewordUtil.getSensitiveWord(string, 2);
        long endTime = System.currentTimeMillis();
        System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
        String s = SensitivewordUtil.replaceSensitiveWord(string, 2, "*");
        System.out.println("替换为:"+s);
        System.out.println("总共消耗时间为：" + (endTime - beginTime));
    }

    //测试排行榜
    void testRow(){

    }

//    用户后台评论测试
    @Test
    void testComment(){
        Page<Comment> page = new Page<>(1, 8);
        IPage<Comment> comByStatus = commentService.findComByStatus(page, 1, null, "", 2);
        System.out.println(comByStatus.getRecords());

    }

    @Test
    void testSQL(){
//        Executors创建线程池，不推荐
//        ThreadPoolExecutor推荐
        System.out.println(dataSource.getClass());
    }

}
