package cn.lyl.news_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: news_system
 * @description: 用户等级
 * @author: lyl
 *
 **/
@TableName("t_level")
@Data
public class Level implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer lid;
    private String lname;
    private Integer level;
}
