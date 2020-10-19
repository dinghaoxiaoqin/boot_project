package com.rrk.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UploadFileService {
    /**
     * 上传图片到服务器
     * @param files
     * @return
     */
    Map<String,Object> addImage(MultipartFile files);

    /**
     * 上传多文件
     * @param files
     * @return
     */
    Map<String, Object> addImages(MultipartFile[] files);
}
