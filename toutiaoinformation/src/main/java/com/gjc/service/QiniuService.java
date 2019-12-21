package com.gjc.service;

import com.gjc.controller.LoginController;
import com.gjc.util.ToutiaoUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    //构造一个带指定 Region 对象的配置类
    //我的是华北地区，所以机房的Region选择Region.region1()，一开始设置的是Region.region1()，所以一直报错
    Configuration cfg = new Configuration(Region.region1());
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);
    //...生成上传凭证，然后准备上传
    String accessKey = "H9TkaP9hBNeE_F2e8ipwOMhMFBsQyOlK-Lp9xCEM";
    String secretKey = "wgVFG4zGtX4Lm-rq2A2rx9A6QUx7ZPqNTFzVARiJ";
    String bucket = "toutiaoinformation";
    //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//    String localFilePath = "/home/qiniu/test.png";
    //默认不指定key的情况下，以文件内容的hash值作为文件名
    String key = null;
    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);



    public String saveImage(MultipartFile file)throws IOException{
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if(dotPos<0){
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos+1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt)){//图片格式不符合
            return null;
        }
        //图片格式符合
        String fileName = UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        try {
            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            System.out.println(response.bodyString());
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
            return ToutiaoUtil.QINIU_DOMAIN_PREFIX+putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            System.err.println(r.bodyString());
            logger.error("七牛异常"+ ex.getMessage());
            return null;
        }
    }
}
