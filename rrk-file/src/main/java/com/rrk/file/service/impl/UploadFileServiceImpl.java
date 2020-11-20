package com.rrk.file.service.impl;

import com.aliyun.oss.OSSClient;
import com.rrk.common.handle.MyException;
import com.rrk.file.config.AliOssProperties;
import com.rrk.file.config.ImageServiceConfig;
import com.rrk.file.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {


    @Autowired
    private ImageServiceConfig imageServiceConfig;

    @Autowired
    private OSSClient ossClient;
    @Autowired
    private AliOssProperties aliOssProperties;
    /**
     * 上传图片服务器
     * @param files
     * @return
     */
    @Override
    public Map<String,Object> addImage(MultipartFile files) {
        String picUrl = "";
        Map<String,Object> resultMap = new HashMap<>();
         StringBuffer buffer = new StringBuffer();
       // for (int i = 0; i < files.length; i++) {
            // 1. 获取该文件的文件名(从页面传递过来的名字)
            String oldFileName = files.getOriginalFilename();
            // 2.创建新的名字(防止不同用户上传的图片名字一致，导致图片被覆盖)
            String newFileName = UUID.randomUUID().toString();
            // 3.新生成的图片名字拼接成图片的类型
            // 4636437326167.jpg
            newFileName = newFileName + oldFileName.substring(oldFileName.lastIndexOf("."));
            // 4.2017/09/27生成用户需要存放的路径，路径以当天的日期为文件夹
           // String filePath = new DateTime().toString("yyyy/MM/dd");
            // 5.开始ftp的上传准备
            try {
                /**
                 * 上传到阿里对象存储空间
                 */
                 picUrl = uploadToOss(newFileName, new ByteArrayInputStream(files.getBytes()));
//                String path ="D:/images/";
//                File dir=new File(path);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//                String filePath = path + newFileName;
//                System.out.println("filePath: "+filePath);
//                //保存文件
//                File dest = new File(filePath);
//                if (!(dest.exists())) {
//                    files.transferTo(dest);
//                }

                /**
                 * 这是上传到lniux
                 */
//                boolean result = FtpUtil.uploadFile(imageServiceConfig.getAddress(),
//                        imageServiceConfig.getPort(), imageServiceConfig.getUsername(),
//                        imageServiceConfig.getPassword(),
//                        imageServiceConfig.getBastPath(), filePath, newFileName, files[i].getInputStream());
//                if (!result) {
//                    throw new MyException(400,"上传失败");
//                }
                //buffer.append(imageServiceConfig.getImageBaseUrl()+"/"+filePath+"/"+newFileName).append(",");
                // String str = "http://localhost:5080/";
                 // buffer.append(str+newFileName).append(",");
            } catch (Exception e) {
                log.error("上传文件异常：{}",e);
                throw new MyException("上传文件异常："+e.getMessage());
            }
       // }
        resultMap.put("fileUrl",picUrl);
        return resultMap;
    }

    private String uploadToOss(String newFileName, InputStream in) {
        // 上传Object.
        ossClient.putObject(aliOssProperties.getBucket(), newFileName, in);
        //返回oss服务器访问上传图片的url
        return "https://" + aliOssProperties.getBucket() + "." + aliOssProperties.getOssUrl()+ "/" + newFileName;
    }

    /**
     * 上传多文件
     * @param files
     * @return
     */
    @Override
    public Map<String, Object> addImages(MultipartFile[] files) {
        Map<String,Object> resultMap = new HashMap<>();
        StringBuffer buffer = new StringBuffer();
        String picUrl = "";
        try {
            for (int i = 0; i < files.length; i++) {
                String oldFileName = files[i].getOriginalFilename();
                // 2.创建新的名字(防止不同用户上传的图片名字一致，导致图片被覆盖)
                String newFileName = UUID.randomUUID().toString();
                newFileName = newFileName + oldFileName.substring(oldFileName.lastIndexOf("."));
                /**
                 * 上传到阿里对象存储空间
                 */
                picUrl = uploadToOss(newFileName, new ByteArrayInputStream(files[i].getBytes()));
//                String path ="D:/images/";
//                File dir=new File(path);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//                String filePath = path + newFileName;
//                //保存文件
//                File dest = new File(filePath);
//                if (!(dest.exists())) {
//                    files[i].transferTo(dest);
//                }
//                String str = "http://localhost:5080/";
                buffer.append(picUrl).append(",");
            }
        } catch (Exception e){
            log.error("上传文件异常：{}",e);
            throw new MyException("上传文件异常："+e.getMessage());
        }
        resultMap.put("fileUrl",buffer.deleteCharAt(buffer.length()-1).toString());
        return resultMap;
    }
}
