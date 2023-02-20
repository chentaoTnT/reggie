package com.itheima.reggie.config;

import com.itheima.reggie.common.JacksonObjectMapper;
import com.itheima.reggie.interceptor.BackendLoginInterceptor;
import com.itheima.reggie.interceptor.FrontLoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射");
        // 配置以下静态资源处理器，可进行静态资源映射
        // 或者在配置文件中配置
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 管理员后端拦截器
        registry.addInterceptor(new BackendLoginInterceptor()).
                addPathPatterns("/admin/**").     //拦截管理员后端所有请求路径
                addPathPatterns("/backend/page/**").
                excludePathPatterns("/admin/employee/**", "/backend/page/login/**").      // 除了登录页
                excludePathPatterns("/backend/api/**", "/backend/images/**", "/backend/js/**", "/backend/plugins/**", "/backend/styles/**", "/backend/favicon.ico");     // 排除静态资源的访问
        // 拦截后端登录请求
        registry.addInterceptor(new BackendLoginInterceptor()).addPathPatterns("/backend/index.html");

        // 用户前端拦截器
        registry.addInterceptor(new FrontLoginInterceptor()).
                addPathPatterns("/user/**").
                addPathPatterns("/front/page/**").
                excludePathPatterns("/front/page/login.html", "/user/user/login").
                excludePathPatterns("/front/api/**", "/front/images/**", "/front/js/**", "/front/fonts/**", "/front/styles/**");
        registry.addInterceptor(new FrontLoginInterceptor()).addPathPatterns("/front/index.html");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin/").setViewName("redirect:/backend/page/login/login.html");
        registry.addViewController("/admin/login").setViewName("redirect:/backend/page/login/login.html");
        registry.addViewController("/user/login").setViewName("redirect:/front/page/login.html");
        registry.addViewController("/user/").setViewName("redirect:/front/page/login.html");
    }

    /**
     * 扩展mvc框架得消息转换器
     * 使得Long转换成String，防止浏览器对Long的精度丢失
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的消息转换器对象追加到mvc框架的转换器容器
        converters.add(0, messageConverter);
    }
}
