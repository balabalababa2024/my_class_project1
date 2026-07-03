package com.wyr.my_class_project.controller;

import com.wyr.my_class_project.common.Result;
import com.wyr.my_class_project.dto.LoginRequest;
import com.wyr.my_class_project.dto.PasswordUpdateRequest;
import com.wyr.my_class_project.dto.RegisterRequest;
import com.wyr.my_class_project.dto.UserDTO;
import com.wyr.my_class_project.entity.User;
import com.wyr.my_class_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<UserDTO> login(@Valid @RequestBody LoginRequest request) {
        UserDTO userDTO = userService.login(request);
        return Result.success(userDTO);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO userDTO = userService.register(request);
        return Result.success(userDTO);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserDTO> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserDTO userDTO = userService.getUserInfo(userId);
        return Result.success(userDTO);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<UserDTO> updateUserInfo(HttpServletRequest request, @RequestBody User user) {
        Long userId = (Long) request.getAttribute("userId");
        UserDTO userDTO = userService.updateUserInfo(userId, user);
        return Result.success(userDTO);
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(HttpServletRequest request, @Valid @RequestBody PasswordUpdateRequest passwordRequest) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updatePassword(userId, passwordRequest);
        return Result.success();
    }
}
