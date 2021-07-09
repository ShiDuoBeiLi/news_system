package cn.wxm.news_demo.service.impl;

import cn.wxm.news_demo.domain.Category;
import cn.wxm.news_demo.mapper.CateMapper;
import cn.wxm.news_demo.service.CateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: news_demo
 * @description:
 * @author: wxm
 * @create: 2021-02-19 16:03
 **/

@Service
public class CateServiceImpl extends ServiceImpl<CateMapper, Category> implements CateService {

}
