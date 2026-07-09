package com.wyr.my_class_project.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.wyr.my_class_project.common.BusinessException;
import com.wyr.my_class_project.common.ResultCode;
import com.wyr.my_class_project.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file, String directory) {
        if (file.isEmpty()) {
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR);
        }

        try {
            // 获取文件内容和MIME类型
            byte[] fileBytes = file.getBytes();
            String contentType = file.getContentType();

            // 转换为Base64
            String base64 = Base64.getEncoder().encodeToString(fileBytes);

            // 返回 data URI 格式
            return "data:" + contentType + ";base64," + base64;
        } catch (IOException e) {
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR);
        }
    }
}
