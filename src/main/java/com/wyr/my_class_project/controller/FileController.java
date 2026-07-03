package com.wyr.my_class_project.controller;

import com.wyr.my_class_project.common.Result;
import com.wyr.my_class_project.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传图片
     */
    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file,
                                               @RequestParam(value = "directory", defaultValue = "images") String directory) {
        String url = fileService.upload(file, directory);

        Map<String, String> result = new HashMap<>();
        result.put("url", url);

        return Result.success(result);
    }

    /**
     * 上传失物图片
     */
    @PostMapping("/upload/lost")
    public Result<Map<String, String>> uploadLostImage(@RequestParam("file") MultipartFile file) {
        String url = fileService.upload(file, "lost");

        Map<String, String> result = new HashMap<>();
        result.put("url", url);

        return Result.success(result);
    }

    /**
     * 上传拾物图片
     */
    @PostMapping("/upload/found")
    public Result<Map<String, String>> uploadFoundImage(@RequestParam("file") MultipartFile file) {
        String url = fileService.upload(file, "found");

        Map<String, String> result = new HashMap<>();
        result.put("url", url);

        return Result.success(result);
    }

    /**
     * 上传证明图片
     */
    @PostMapping("/upload/proof")
    public Result<Map<String, String>> uploadProofImage(@RequestParam("file") MultipartFile file) {
        String url = fileService.upload(file, "proof");

        Map<String, String> result = new HashMap<>();
        result.put("url", url);

        return Result.success(result);
    }
}
