package com.gjc.controller;

import com.gjc.model.HostHolder;
import com.gjc.model.News;
import com.gjc.service.AliService;
import com.gjc.service.NewsService;
import com.gjc.service.QiniuService;
import com.gjc.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

@Controller
public class NewsController {
    @Autowired
    NewsService newsService;

    @Autowired
    QiniuService qiniuService;

    @Autowired
    AliService aliService;

    @Autowired
    HostHolder hostHolder;

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @RequestMapping(path={"/user/addNews/"},method={RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try {
            News news = new News();
            if(hostHolder.getUser()!=null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                //匿名id,自己定义好
                news.setUserId(3);
            }
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);

        } catch (Exception e) {
            logger.error("添加资讯错误"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发布失败");

        }
    }

    @RequestMapping(path={"/uploadImage/"},method={RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try {
            //本地上传
//            String fileUrl = newsService.saveImage(file);
            //七牛云上传
            String fileUrl = qiniuService.saveImage(file);
            //阿里云上传
//            String fileUrl = aliService.saveImage(file);
            if(fileUrl==null){
                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0,fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传图片失败");
        }
    }

    @RequestMapping(path={"/uploadImages/"},method={RequestMethod.POST})
    @ResponseBody
    public String uploadImages(@RequestParam("files") MultipartFile[]files){
        try {
            String fileUrl = newsService.saveImages(files);
            if(fileUrl==null){
                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0,fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传图片失败");
        }
    }

    //从本地下载图片
    @RequestMapping(path={"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response){
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(ToutiaoUtil.IMAGE_DIR+imageName)), response.getOutputStream());
        } catch (IOException e) {
            logger.error("读取图片错误"+e.getMessage());
        }
    }


}
