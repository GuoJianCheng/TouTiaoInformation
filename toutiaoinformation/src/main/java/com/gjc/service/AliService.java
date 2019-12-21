package com.gjc.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.gjc.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class AliService {
    private static final Logger logger = LoggerFactory.getLogger(AliService.class);
    private static final String url = "http://toutiaoinformation.oss-cn-beijing.aliyuncs.com/";
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    private static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private static final String accessKeyId = "LTAI4Fivoz7uELj9xV7LEWcd";
    private static final String accessKeySecret = "PvEMWrb6qN9I9mEhzSArUhGlwfsKvd";
    private static final String bucketName = "toutiaoinformationpublic";


    public String saveImage(MultipartFile file) throws IOException {
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

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file.getInputStream());
        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        try {
            PutObjectResult res = ossClient.putObject(putObjectRequest);
            if(res==null){
                return null;
            }
            System.out.println(res.getETag());
            return url+fileName;
        } catch (OSSException e) {
            logger.error("上传失败" + e.getMessage());
        }
        // 关闭OSSClient。
        ossClient.shutdown();
        return null;
    }

}
