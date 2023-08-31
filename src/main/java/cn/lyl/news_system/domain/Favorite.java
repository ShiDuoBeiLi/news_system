package cn.lyl.news_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: news_system
 * @description: 用户收藏实体类
 * @author: lyl
 *
 **/
@Data
@TableName("t_favorite")
public class Favorite implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer fid;//主键
   private Integer uid;//用户id
   private Integer aid;//文章id
    private Date addTime;//添加时间

    public Integer getAid() {
        return aid;
    }
}
