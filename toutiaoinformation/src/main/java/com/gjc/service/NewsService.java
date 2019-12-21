package com.gjc.service;

import com.gjc.dao.NewsDAO;
import com.gjc.model.News;
import com.gjc.util.ToutiaoUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Resource
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }

    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int id){
        return newsDAO.getById(id);
    }

    public int updateCommentCount(int id, int commentCount){
        return newsDAO.updateCommentCount(id, commentCount);
    }

    public int updateLikeCount(int id, int likeCount){
        return newsDAO.updateLikeCount(id, likeCount);
    }

    //上传单张图片
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
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+fileName;
    }

    //上传多张图片
    public String saveImages(MultipartFile[]files) throws IOException{
        StringBuffer sb = new StringBuffer();
        for(MultipartFile file:files){
            String fileUrl = saveImage(file);
            sb.append(fileUrl);
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }



}
