package cn.lyl.news_system.service;

import cn.lyl.news_system.domain.ArtImage;
import cn.lyl.news_system.domain.Article;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ArtImageService extends IService<ArtImage> {
    public void saveArtImage(Article article);
}
