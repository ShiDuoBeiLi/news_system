package cn.wxm.news_demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: news_demo
 * @description: 用户收藏实体类
 * @author: wxm
 * @create: 2021-02-23 10:54
 **/
@Data
@TableName("t_favorite")
public class Favorite implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer fid;//主键
   private Integer uid;//用户id
   private Integer aid;//文章id
    private Date addTime;//添加时间
}
