package cn.wxm.news_demo.mapper;

import cn.wxm.news_demo.domain.Article;
import cn.wxm.news_demo.domain.Comment;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    public List<Article> addCateAll();

    //分页
    IPage<Article> findByPage(IPage<Article> page, @Param("ew") QueryWrapper<Article> queryWrapper) throws Exception;

    //通过id查找文章详细信息，多表查询
    Article findMessageId(Integer aid);

    //动态sql，查找未审核文章的多表查询
    IPage<Article> findAllByStatus(IPage<Article> page, @Param("uname") String uname, @Param("cname") String cname,
                                   @Param("title") String title, @Param("lid") Integer lid, @Param("status") Integer status);

    //    List<Article> findAllByStatus(@Param("uname")String uname,@Param("cname")String cname,
//                                          @Param("title")String title,@Param("lid")Integer lid);

}
