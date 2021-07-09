package cn.wxm.news_demo.service.impl;

import cn.wxm.news_demo.domain.Category;
import cn.wxm.news_demo.domain.Favorite;
import cn.wxm.news_demo.mapper.CateMapper;
import cn.wxm.news_demo.mapper.FavoriteMapper;
import cn.wxm.news_demo.service.CateService;
import cn.wxm.news_demo.service.FavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: news_demo
 * @description:
 * @author: wxm
 * @create: 2021-02-19 16:03
 **/

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

}
