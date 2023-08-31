package cn.lyl.news_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: news_system
 * @description: 新闻路径实体类
 * @author: lyl
 *
 **/
@Data
@TableName("t_article_image")
public class ArtImage  implements Serializable {
    @TableId(type=IdType.AUTO)
    private Integer imageId;
    private String imageSrc;
    private Integer artId;
}
