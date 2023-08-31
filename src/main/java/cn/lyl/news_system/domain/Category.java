package cn.lyl.news_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

/**
 * @program: news_system
 * @description: 类别
 * @author: lyl
 *
 **/
@Data
@TableName("t_category")
public class Category implements Serializable {
    @TableId(type= IdType.AUTO)
    private Integer cid;
    private String cname;

}
