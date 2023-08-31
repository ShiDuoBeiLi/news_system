package cn.lyl.news_system.controller;

import cn.lyl.news_system.domain.Article;
import cn.lyl.news_system.domain.Category;
import cn.lyl.news_system.domain.Comment;
import cn.lyl.news_system.domain.User;
import cn.lyl.news_system.service.ArticleService;
import cn.lyl.news_system.service.CateService;
import cn.lyl.news_system.service.CommentService;
import cn.lyl.news_system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @program: demo
 * @description: 文章管理
 * @author: lyl
 *
 **/
@Controller
@Slf4j
public class ArticleController {
    @Autowired
    ArticleService articleService;

    @Autowired
    CateService cateService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @RequestMapping("article_pub")
    public String article_Pub(@RequestParam(value = "id",required = false)Integer id,Model model) {
        Article byId = articleService.getById(id);
        model.addAttribute("byId",byId);

        List<Category> categories = cateService.list();
        model.addAttribute("categories",categories);
        return "article_pub";
    }

    //上传图片等资源文件
    @ResponseBody
    @RequestMapping(value = "/upload/uploadFile")
    public Map uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        if (file != null) {
//            String webapp = request.getSession().getServletContext().getRealPath("/");
            //存放到项目静态资源下
            String webapp = "src/main/resources/";
            try {
                //图片名字
                String substring = file.getOriginalFilename();
//                System.out.println(substring);
                //使用uuid替代原来名字
                String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                // 图片的路径+文件名称
//                String fileName = "/static/upload/" + substring;
                //使用uuid上传将上传的图片重命名，但是遇到改名之后上传较慢，需要等待传输才能回显
                String uuidName = uuid + "." + substring.substring(substring.lastIndexOf(".") + 1);
//                System.out.println(uuidName);
                String fileName = "/static/upload/" + uuidName;
//                System.out.println(fileName);
                // 图片的在服务器上面的物理路径
                File destFile = new File(webapp, fileName);
//                log.info("真实路径{}",destFile);
                // 生成upload目录
                File parentFile = destFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();// 自动生成upload目录
                }
                // 把上传的临时图片，复制到当前项目的webapp路径
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(destFile));
                map = new HashMap<>();
                map2 = new HashMap<>();
                map.put("code", 0);//0表示成功，1失败
                map.put("msg", "上传成功");//提示消息
                map.put("data", map2);
//                map2.put("src", fileName);//图片url
//                map2.put("src", "/upload/" + substring);//图片url
                map2.put("src", "/upload/"+uuidName);//图片url
//                map2.put("title", substring);//图片名称，这个会显示在输入框里
                map2.put("title", uuidName);//图片名称，这个会显示在输入框里
                log.info("图片地址为{}",uuidName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    //文章保存
    @PostMapping("/articleSave")
    @ResponseBody
    public Map articleSave(@RequestBody Article article, HttpSession session) {

        Map map1 = articleService.savaArticle(article, session);

        return map1;
    }


    //通过列表点击文章跳转
    @GetMapping("/article/details/{id}")
    public String findArticalDetail(@PathVariable("id")Integer id,Model model){
//        Article articleById = articleService.getById(id);
//        //点击数+1
//        articleById.setCheckNum(articleById.getCheckNum()+1);
//        articleService.updateById(articleById);
        //获取点击新闻的内容
        Article articleById = articleService.findMessageId(id);
        //获取该新闻下评论
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        List<Comment> commentList = commentService.list(wrapper.eq("aid", id).orderByAsc("com_time"));
        //获取相关评论的用户信息
        List<User> comUser = commentService.getComUser(id);

        model.addAttribute("articleById",articleById);
        model.addAttribute("commentList",commentList);
        model.addAttribute("comUser",comUser);
        return "article_details";
    }


    //访问指定新闻分类
    @GetMapping("/cate/CateArticle/{cid}")
     public String CateArticle(@PathVariable("cid")Integer cid, Model model){

        Category cateById = cateService.getById(cid);
        model.addAttribute("cateById",cateById);

        return "article";
    }

    //新闻分类查询结果返回分页结果
    @RequestMapping("/cate/queryCaetAll")
    @ResponseBody
    public Map queryCateAll(Integer pageNum,Integer pageSize,Integer cateId,HttpSession session) {

        //使用MP自带的分页插件分页查询数据
        Page<Article> catePage = new Page<>(pageNum, pageSize);
        //分页查询结果
        QueryWrapper<Article> cateQuery = new QueryWrapper<>();
        //查询条件,用户有登录显示为用户等级的查询，没有登录查询的是最低的等级
        User user = (User) session.getAttribute("user");
        if (user!=null){
            cateQuery.eq("cid",cateId).eq("lid",user.getLid())
                    .eq("status",1).orderByDesc("create_time");
        }else {
            cateQuery.eq("cid",cateId).eq("lid",1)
                    .eq("status",1).orderByDesc("create_time");
        }

        Page<Article> catepages = articleService.page(catePage, cateQuery);
        List<Article> cateArtList = catepages.getRecords();

        long total = catepages.getTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("data",cateArtList);
        map.put("count",total);
        map.put("status",200);
//        map.put("cate",cate);
        return map;
    }


//    进行新闻收藏
    @PostMapping("/article/fav")
    @ResponseBody
    public Map artFavorite(Integer id,HttpSession session){
        Map map = articleService.artFavorite(id, session);
        return map;
    }

    //新闻搜索
    @PostMapping("/article/search")
    public String search(String keyword,Model model,HttpSession session){
        //返回搜素的内容
        model.addAttribute("keyword",keyword);
        //返回搜素结果
        List<Article> articles = articleService.searchArt(session, keyword);
        model.addAttribute("articles",articles);
        return "search";
    }

    //展示其他等级新闻
    @RequestMapping("/article/other/{lid}")
    public  String otherArticle(@PathVariable("lid")Integer lid,Model model,
                                @RequestParam(value = "level",required = false)Integer level,
                                @RequestParam(value = "cid",required = false)Integer cid,
                                @RequestParam(value = "keyword",required = false)String keyword){
//                                @RequestParam(value = "pageNum")Integer pageNum,
//                                @RequestParam(value = "pageSize")Integer pageSize){
        model.addAttribute("level",lid);
        model.addAttribute("cid",cid);
        model.addAttribute("keyword",keyword);
//        model.addAttribute("level",lid);
//        model.addAttribute("level",lid);

//        log.info("{}",level);
//        log.info("{}",cid);
//        log.info("{}",keyword);
        return "other_level";
    }


    //返回其他等级查询结果，可分页
    @RequestMapping("/article/other/levelResult")
    @ResponseBody
    public Map queryOtherLevResult(Integer pageNum,Integer pageSize,Integer cid,Integer level,
                                   String keyword) {
        Map<String, Object> map = new HashMap<>();
        try {
            IPage<Article> page = articleService.findOtherLevPage(pageNum, pageSize,cid,level,keyword);
            List<Article> addCateList = page.getRecords();
            long total = page.getTotal();
            map.put("data",addCateList);
            map.put("count",total);
            map.put("status",200);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return map;

//        System.out.println(pageNum+":"+pageSize+":"+cid+":"+level+":"+keyword);
//        //使用MP自带的分页插件分页查询数据
//        Page<Article> levelPage = new Page<>(pageNum, pageSize);
//        //分页查询结果
////        QueryWrapper<Article> cateQuery = new QueryWrapper<>();
//        QueryWrapper<Article> wrapper = new QueryWrapper<>();
//        //查询条件
//        wrapper.eq("lid",level);
//        //条件筛选
//        if (StringUtils.isNotBlank(keyword)){
//            wrapper.like("title",keyword).or().like("content",keyword);
//        }
//        if (cid!=null){
//            wrapper.eq("cid",cid);
//        }
//
//        Page<Article> levpages = articleService.page(levelPage, wrapper);
//        List<Article> levArtList = levpages.getRecords();
//
//        long total = levpages.getTotal();
//        Map<String, Object> map = new HashMap<>();
//        map.put("data",levArtList);
//        map.put("count",total);
//        map.put("status",200);
//        map.put("cate",cate);
//        return map;
    }


}
