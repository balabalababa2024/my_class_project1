package com.wyr.my_class_project.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String studentId;

    private String name;

    private String phone;

    private String avatar;

    private String college;

    private String email;

    private Integer role;

    private String token;
}
