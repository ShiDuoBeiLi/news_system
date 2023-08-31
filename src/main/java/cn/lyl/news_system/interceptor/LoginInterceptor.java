package cn.lyl.news_system.interceptor;

import cn.lyl.news_system.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program:
 * @description: 登录拦截，看是否有登录账号
 * 登录检查
 * 1.配置好拦截器要拦截哪些请求
 * 2.把这些请求放到容器中
 * @author: lyl
 *
 **/
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 登录之前拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @date 2023/2/14 16:02
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("拦截的请求路径为{}",requestURI);
        User user = (User)request.getSession().getAttribute("back_user");
        if (user != null) {
            //是否激活
            if (user.getLid()>=2){
                return true;
            }else {
                return false;
            }
        }else {
            //拦截住,未登录，跳转到登录页
            request.setAttribute("msg", "请先激活");
//        response.sendRedirect("/");
            request.getRequestDispatcher("/back/login").forward(request, response);
            return false;
        }
    }

    /**
     * 目标方法执行完成以后
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @return
     * @date 2023/2/14 16:03
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 页面渲染之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     * @date 2023/2/14 16:04
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
