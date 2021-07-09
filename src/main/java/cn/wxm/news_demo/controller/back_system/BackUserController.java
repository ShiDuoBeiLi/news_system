package cn.wxm.news_demo.controller.back_system;

import cn.wxm.news_demo.domain.User;
import cn.wxm.news_demo.service.UserService;
import cn.wxm.news_demo.utils.MD5Util;
//import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: news_demo
 * @description: 后台用户管理
 * @author: wxm
 * @create: 2021-02-28 17:17
 **/
@Controller
@RequestMapping("/back/user")
@Slf4j
public class BackUserController {
    @Autowired
    UserService userService;


    //后台用户管理请求
    @PostMapping({"/loginResult"})
    @ResponseBody
    public Map sendLoginResult(@RequestBody Map<String,String> map, HttpSession session){
        Map resultMap = userService.backLoginResult(map, session);

        return resultMap;
    }

    //用户退出
    @RequestMapping("/quit")
    public String quit(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    //返回用户列表，如有条件则添加
    @RequestMapping("/manage")
    public String userManage(@RequestParam(value = "pn",defaultValue = "1")Integer pn,
                             @RequestParam(value = "uname",required = false)String uname,
                             @RequestParam(value = "lid",required = false)Integer lid,
                             @RequestParam( value = "active",required = false)Integer active,
                             Model model, HttpSession session){

        //默认第一页开始，一页20条
        IPage<User> page = new Page<>(pn, 20);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //条件筛选
        if (StringUtils.isNotBlank(uname)){
            log.info("1");
            queryWrapper.like("uname",uname);
        }
        if (lid!=null){
            queryWrapper.eq("lid",lid);
            log.info("2");
        }
        if (active!=null){
            queryWrapper.eq("active",active);
            log.info("3");
        }
        User back_user = (User) session.getAttribute("back_user");
        //展示小于用户等级的数据
        queryWrapper.lt("lid",back_user.getLid());
//        System.out.println(queryWrapper);
        IPage<User> userIPage = userService.page(page, queryWrapper);
        //返回数据和总页数
        model.addAttribute("userIPage",userIPage);
        long pages = userIPage.getPages();
        model.addAttribute("pages",pages);

        return "/back/user-manage";
    }

    //添加新用户页面
    @RequestMapping("/addUser")
    public String addUser(){
        return "/back/user-add";
    }

    //添加新用户和编辑结果的接收与返回
    @PostMapping("/addOne")
    @ResponseBody
    public Map addOneResult(@RequestBody User user){
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uname",user.getUname());
        //判断是否存在相同的人
        User one = userService.getOne(wrapper);
        String msg=null;
        Integer status;
        if (one!=null){
          status=400;
          msg="该用户账号已存在，请选择其他的用户账号";
        }else{
            status=200;
            msg="添加用户成功";
            user.setCreateTime(new Date());
            user.setNewsName("暂未设置用户昵称");
            userService.save(user);
        }
//        log.info("添加的用户是{}",user);
        Map<String, Object> map = new HashMap<>();
        map.put("status",status);
        map.put("msg",msg);
        return map;
    }

    //用户删除
    @PostMapping("/deleteUser")
    @ResponseBody
    public String returnDelResult(Integer uid){
        userService.removeById(uid);
        return "success";
    }

    //用户编辑
    @RequestMapping("/editUser")
    public String editUser(Integer uid,Model model){
        User userInfo = userService.getById(uid);
        model.addAttribute("userInfo",userInfo);
        return "/back/user-info";
    }
    //用户编辑页面提交结果返回
    @PostMapping("/editResult")
    @ResponseBody
    public Map editResult(@RequestBody User user,Model model){
//        log.info("提交的数据是{}",user);
        //需要将修改的密码MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        //获取原来的用户昵称
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        User byId = userService.getById(user.getUid());
        user.setNewsName(byId.getNewsName());
        boolean b = userService.updateById(user);
        Integer status;
        String msg;
        if (b){
            status=200;
            msg="用户信息修改成功";
        }else {
            status=400;
            msg="出现错误，请稍后尝试";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("status",status);
        map.put("msg",msg);
        return map;
    }

    //用户数据导出excel
    @PostMapping("/exportUserData")
    @ResponseBody
    public List<User> exportUserData(){
        List<User> userList = userService.list();
        return userList;

    }

}
