package cn.wxm.news_demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.javamail.MimeMailMessage;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: news_demo
 * @description: 用户实体类
 * @author: wxm
 * @create: 2021-02-23 10:54
 **/
@Data
@TableName("t_user")
public class User  implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer uid;//主键
    private String uname;
    private String password;
    private String telephone;
    private String email;
    private String sex;//
    private Integer lid;//等级
    private Integer active;//激活状态
    private String code;//激活码，普通用户需要，系统用户不需要
    private String newsName;//新闻用户昵称
//    private String head_image;//用户头像
    private String headImage;//用户头像
    private Integer adRole;//是否管理员
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间
}
