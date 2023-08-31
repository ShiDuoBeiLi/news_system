package cn.lyl.news_system.service.impl;

import cn.lyl.news_system.domain.Category;
import cn.lyl.news_system.mapper.CateMapper;
import cn.lyl.news_system.service.CateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: news_system
 * @description:
 * @author: lyl
 *
 **/

@Service
public class CateServiceImpl extends ServiceImpl<CateMapper, Category> implements CateService {

}
