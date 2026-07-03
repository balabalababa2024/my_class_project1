package com.wyr.my_class_project.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.wyr.my_class_project.common.BusinessException;
import com.wyr.my_class_project.common.ResultCode;
import com.wyr.my_class_project.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    @Override
    public String upload(MultipartFile file, String directory) {
        if (file.isEmpty()) {
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR);
        }

        // 获取文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtil.extName(originalFilename);

        // 生成唯一文件名
        String fileName = IdUtil.fastSimpleUUID() + "." + suffix;

        // 创建目录
        String dirPath = uploadPath + directory;
        FileUtil.mkdir(dirPath);

        // 保存文件
        try {
            file.transferTo(new File(dirPath + File.separator + fileName));
        } catch (IOException e) {
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR);
        }

        // 返回访问URL
        return urlPrefix + directory + "/" + fileName;
    }
}
