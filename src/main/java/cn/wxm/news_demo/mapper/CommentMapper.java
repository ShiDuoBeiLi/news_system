package cn.wxm.news_demo.mapper;

import cn.wxm.news_demo.domain.Category;
import cn.wxm.news_demo.domain.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

public interface CommentMapper extends BaseMapper<Comment> {
    //动态sql，查找未审核文章的多表查询
    IPage<Comment> findComByStatus(IPage<Comment> page, @Param("lid") Integer lid,
                                   @Param("illegal") Integer illegal, @Param("uname") String uname,
                                   @Param("userLid")Integer userLid);
}
