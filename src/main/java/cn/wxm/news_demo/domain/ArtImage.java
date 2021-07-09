package cn.wxm.news_demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: news_demo
 * @description: 新闻路径实体类
 * @author: wxm
 * @create: 2021-02-20 00:39
 **/
@Data
@TableName("t_article_image")
public class ArtImage  implements Serializable {
    @TableId(type=IdType.AUTO)
    private Integer imageId;
    private String imageSrc;
    private Integer artId;
}
