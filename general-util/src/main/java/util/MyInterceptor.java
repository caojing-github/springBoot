package util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static util.GlobalExceptionHandler.REQUEST_BODY;

/**
 * 自定义拦截器 http://loveshisong.cn/%E7%BC%96%E7%A8%8B%E6%8A%80%E6%9C%AF/2016-11-18-Spring-boot%E4%B8%8EHandlerInterceptor.html
 *
 * @author CaoJing
 * @date 2019/12/29 19:13
 */
@Component
public class MyInterceptor extends HandlerInterceptorAdapter {

    /**
     * 在Controller处理之前调用, 返回false时整个请求结束
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    /**
     * 在Controller调用之后执行, 但它会在DispatcherServlet进行视图的渲染之前执行, 也就是说在这个方法中你可以对ModelAndView进行操作
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 在整个请求完成之后执行, 也就是DispatcherServlet已经渲染了视图之后执行; 这个方法的主要作用是用于清理资源的
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        REQUEST_BODY.remove();
    }

    /**
     * 这个方法是AsyncHandlerInterceptor接口中添加的. 当Controller中有异步请求方法的时候会触发该方法, 异步请求先支持preHandle、然后执行afterConcurrentHandlingStarted, 异步线程完成之后执行会再执行preHandle、postHandle、afterCompletion
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}