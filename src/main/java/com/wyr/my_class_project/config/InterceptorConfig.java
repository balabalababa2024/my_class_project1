package com.wyr.my_class_project.config;

import com.wyr.my_class_project.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
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
    }
}
