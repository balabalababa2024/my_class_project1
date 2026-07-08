package com.wyr.my_class_project.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyr.my_class_project.annotation.RequireAdmin;
import com.wyr.my_class_project.common.Constants;
import com.wyr.my_class_project.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员权限拦截器
 * 检查请求是否具有管理员权限
 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 只处理控制器方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 检查方法或类上是否有@RequireAdmin注解
        RequireAdmin requireAdmin = handlerMethod.getMethodAnnotation(RequireAdmin.class);
        if (requireAdmin == null) {
            requireAdmin = handlerMethod.getBeanType().getAnnotation(RequireAdmin.class);
        }

        // 如果没有@RequireAdmin注解，直接放行
        if (requireAdmin == null) {
            return true;
        }

        // 获取用户角色
        Integer role = (Integer) request.getAttribute("role");

        // 验证管理员权限
        if (role == null || role != Constants.ROLE_ADMIN) {
            sendError(response, 403, "权限不足，需要管理员权限");
            return false;
        }

        return true;
    }

    private void sendError(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        Result<?> result = Result.error(code, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
