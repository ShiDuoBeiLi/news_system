package cn.wxm.news_demo.service.impl;

import cn.wxm.news_demo.domain.ArtImage;
import cn.wxm.news_demo.domain.Article;
import cn.wxm.news_demo.domain.Category;
import cn.wxm.news_demo.mapper.ArtImageMapper;
import cn.wxm.news_demo.mapper.ArticleMapper;
import cn.wxm.news_demo.mapper.CateMapper;
import cn.wxm.news_demo.service.ArtImageService;
import cn.wxm.news_demo.service.CateService;
import cn.wxm.news_demo.utils.ImageUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: news_demo
 * @description:
 * @author: wxm
 * @create: 2021-02-19 16:03
 **/

@Service
public class ArtImageServiceImpl extends ServiceImpl<ArtImageMapper, ArtImage> implements ArtImageService {
    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArtImageMapper imageMapper;

    //查找新闻图片的路径
    public void saveArtImage(Article article){
        String content = article.getContent();
        List<String> imgStr = ImageUtil.getImgStr(content);


        ArtImage artImage = new ArtImage();
        if (imgStr==null||imgStr.size()==0){
            artImage.setArtId(article.getId());
            artImage.setImageSrc("/images/white.jpg");
        }else {
            //取第一张图片即可
            String src = imgStr.get(0);
            artImage.setArtId(article.getId());
            artImage.setImageSrc(src);
        }

//
//        //取第一张图片即可
//        ArtImage artImage = new ArtImage();
//        String src = imgStr.get(0);
//        //判断
//        if (src==null||src.equals("")){
//            artImage.setArtId(article.getId());
//            artImage.setImageSrc("无");
//        }else {
//            artImage.setArtId(article.getId());
//            artImage.setImageSrc(src);
//        }

        imageMapper.insert(artImage);
    }

}
