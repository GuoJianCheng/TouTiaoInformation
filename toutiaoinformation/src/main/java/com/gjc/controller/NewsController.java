package com.gjc.controller;

import com.gjc.model.*;
import com.gjc.service.*;
import com.gjc.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @RequestMapping(path={"/news/{newsId}"},method={RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        News news = newsService.getById(newsId);
        if(news!=null){
            int localUserId = hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
            if(localUserId != 0){
                //登录状态
                model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
            }else{
                model.addAttribute("like",0);
            }
            //评论
            List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVos = new ArrayList<ViewObject>();
            for(Comment comment: comments){
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                commentVos.add(vo);
            }
            model.addAttribute("comments", commentVos);
        }
        model.addAttribute("news",news);
        model.addAttribute("owner",userService.getUser(news.getUserId()));
        return "detail";
    }

    @RequestMapping(path={"/addComment"},method={RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content){
        try {
            //未来这里需要过滤评论
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);
            //更新news里的评论数量
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);
            //怎么异步化
        } catch (Exception e) {
            logger.error("提交评论失败 "+e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }


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
