package cn.lyl.news_system.service.impl;

import cn.lyl.news_system.domain.Favorite;
import cn.lyl.news_system.mapper.FavoriteMapper;
import cn.lyl.news_system.service.FavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: news_system
 * @description:
 * @author: lyl
 *
 **/

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

}
