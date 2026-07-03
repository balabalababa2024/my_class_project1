package com.wyr.my_class_project.service;

import com.wyr.my_class_project.entity.User;
import com.wyr.my_class_project.dto.LoginRequest;
import com.wyr.my_class_project.dto.RegisterRequest;
import com.wyr.my_class_project.dto.UserDTO;
import com.wyr.my_class_project.dto.PasswordUpdateRequest;

public interface UserService {

    /**
     * 用户登录
     */
    UserDTO login(LoginRequest request);

    /**
     * 用户注册
     */
    UserDTO register(RegisterRequest request);

    /**
     * 获取用户信息
     */
    UserDTO getUserInfo(Long userId);

    /**
     * 更新用户信息
     */
    UserDTO updateUserInfo(Long userId, User user);

    /**
     * 修改密码
     */
    void updatePassword(Long userId, PasswordUpdateRequest request);

    /**
     * 根据ID获取用户
     */
    User getById(Long userId);
}
