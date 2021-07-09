package cn.wxm.news_demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: news_demo
 * @description: 用户评论
 * @author: wxm
 * @create: 2021-02-27 20:51
 **/
@TableName("t_comment")
@Data
public class Comment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer comid;//评论表主键
    private Integer aid;//评论新闻的id
    private Integer uid;//评论人id
    private String content;//评论内容
    private Date comTime;//评论时间
    private  Integer illegal;//用户评论是否非法，即包含敏感字符串

    @TableField(exist = false)
    private User user;//冗余字段，查询用户信息

}
