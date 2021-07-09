package cn.wxm.news_demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: news_demo
 * @description: 用户等级
 * @author: wxm
 * @create: 2021-02-27 20:51
 **/
@TableName("t_level")
@Data
public class Level implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer lid;
    private String lname;
    private Integer level;
}
