package com.wyr.my_class_project.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 上传文件
     * @param file 文件
     * @param directory 目录
     * @return 文件访问URL
     */
    String upload(MultipartFile file, String directory);
}
