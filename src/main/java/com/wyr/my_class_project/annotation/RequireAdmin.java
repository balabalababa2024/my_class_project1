package com.wyr.my_class_project.annotation;

import java.lang.annotation.*;

/**
 * 管理员权限注解
 * 标记需要管理员权限才能访问的接口
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireAdmin {
}
