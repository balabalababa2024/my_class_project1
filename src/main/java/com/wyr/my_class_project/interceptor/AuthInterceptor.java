package com.wyr.my_class_project.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyr.my_class_project.common.Result;
import com.wyr.my_class_project.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 获取Token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证Token
        if (token == null || !jwtConfig.validateToken(token)) {
            sendError(response, 401, "未登录或登录已过期");
            return false;
        }

        // 将用户信息存入请求属性
        Long userId = jwtConfig.getUserIdFromToken(token);
        String studentId = jwtConfig.getStudentIdFromToken(token);
        Integer role = jwtConfig.getRoleFromToken(token);

        request.setAttribute("userId", userId);
        request.setAttribute("studentId", studentId);
        request.setAttribute("role", role);

        return true;
    }

    private void sendError(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        Result<?> result = Result.error(code, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
