package com.wyr.my_class_project.config;

import com.wyr.my_class_project.interceptor.AdminAuthInterceptor;
import com.wyr.my_class_project.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private AdminAuthInterceptor adminAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录认证拦截器
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/user/**", "/lost/**", "/found/**", "/match/**", "/claim/**", "/notification/**", "/announcement/create", "/announcement/update", "/announcement/delete", "/announcement/admin/**", "/file/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/announcement/list",
                        "/announcement/detail/**",
                        "/statistics/**",
                        "/category/list",
                        "/files/**",
                        "/error",
                        "/",
                        "/index.html",
                        "*.html",
                        "*.js",
                        "*.css",
                        "*.png",
                        "*.jpg",
                        "*.gif",
                        "*.ico"
                );

        // 管理员权限拦截器（在登录认证之后执行）
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/announcement/admin/**", "/announcement/create", "/announcement/update", "/announcement/delete");
    }
}
