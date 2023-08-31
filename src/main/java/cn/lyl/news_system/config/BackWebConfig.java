package cn.lyl.news_system.config;


import cn.lyl.news_system.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program:
 * @description: 配置文件
 * @author: lyl
 *
 **/

@Configuration
public class BackWebConfig implements WebMvcConfigurer {
    //添加拦截器到配置文件中
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())   //注册拦截器
            .addPathPatterns("/back/**")       //添加需要拦截的路径，这里为所有请求都被拦截，包括静态
            .excludePathPatterns("/back/login","/back/user/loginResult","/css/**","/fonts/**","/images/**","/js/**",
                    "/xadmin/**","/layer/**","/upload/**");    //需要放行的资源和页面


    }
}
