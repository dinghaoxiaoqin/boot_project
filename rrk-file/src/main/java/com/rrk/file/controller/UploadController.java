package com.rrk.file.controller;

import com.rrk.common.R;
import com.rrk.file.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

/**
 * 上传图片的前端控制器
 */
@RestController
@RequestMapping(value ="/upload")
@CrossOrigin
@Slf4j
public class UploadController {

    @Autowired
    private UploadFileService uploadFileService;

    /**
     * 上传图片到服务器
     */

    @PostMapping(value = "/addImage")
    public R<Object> addImage(MultipartHttpServletRequest multiReq) {
        MultipartFile file = multiReq.getFile("file");
        Map<String,Object> imagePath = uploadFileService.addImage(file);
        return R.ok(200,"上传成功",imagePath);
    }
    /**
     * 上传多文件
     */
    @PostMapping(value = "/addImages")
    public R<Object> addImages(@RequestParam("file") MultipartFile[] files){
        Map<String,Object> imagePath = uploadFileService.addImages(files);
        return R.ok(200,"上传成功",imagePath);
    }
}
