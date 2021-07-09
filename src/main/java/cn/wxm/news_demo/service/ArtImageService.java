package cn.wxm.news_demo.service;

import cn.wxm.news_demo.domain.ArtImage;
import cn.wxm.news_demo.domain.Article;
import cn.wxm.news_demo.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ArtImageService extends IService<ArtImage> {
    public void saveArtImage(Article article);
}
