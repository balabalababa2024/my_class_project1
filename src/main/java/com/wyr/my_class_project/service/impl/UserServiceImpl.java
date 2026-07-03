package com.wyr.my_class_project.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wyr.my_class_project.common.BusinessException;
import com.wyr.my_class_project.common.ResultCode;
import com.wyr.my_class_project.config.JwtConfig;
import com.wyr.my_class_project.dto.LoginRequest;
import com.wyr.my_class_project.dto.PasswordUpdateRequest;
import com.wyr.my_class_project.dto.RegisterRequest;
import com.wyr.my_class_project.dto.UserDTO;
import com.wyr.my_class_project.entity.User;
import com.wyr.my_class_project.mapper.UserMapper;
import com.wyr.my_class_project.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public UserDTO login(LoginRequest request) {
        // 根据学号查找用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getStudentId, request.getStudentId())
        );

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 验证密码
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }

        // 生成Token
        String token = jwtConfig.generateToken(user.getId(), user.getStudentId(), user.getRole());

        // 转换DTO
        UserDTO dto = convertToDTO(user);
        dto.setToken(token);

        return dto;
    }

    @Override
    public UserDTO register(RegisterRequest request) {
        // 检查学号是否已存在
        User existUser = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getStudentId, request.getStudentId())
        );

        if (existUser != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }

        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setRole(0);  // 默认普通用户
        user.setStatus(1); // 默认正常状态

        userMapper.insert(user);

        // 生成Token
        String token = jwtConfig.generateToken(user.getId(), user.getStudentId(), user.getRole());

        // 转换DTO
        UserDTO dto = convertToDTO(user);
        dto.setToken(token);

        return dto;
    }

    @Override
    public UserDTO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO updateUserInfo(Long userId, User updateUser) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 只允许更新部分字段
        if (updateUser.getName() != null) {
            user.setName(updateUser.getName());
        }
        if (updateUser.getPhone() != null) {
            user.setPhone(updateUser.getPhone());
        }
        if (updateUser.getCollege() != null) {
            user.setCollege(updateUser.getCollege());
        }
        if (updateUser.getEmail() != null) {
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getAvatar() != null) {
            user.setAvatar(updateUser.getAvatar());
        }

        userMapper.updateById(user);

        return convertToDTO(user);
    }

    @Override
    public void updatePassword(Long userId, PasswordUpdateRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 验证原密码
        if (!BCrypt.checkpw(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }

        // 更新密码
        user.setPassword(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
        userMapper.updateById(user);
    }

    @Override
    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}
