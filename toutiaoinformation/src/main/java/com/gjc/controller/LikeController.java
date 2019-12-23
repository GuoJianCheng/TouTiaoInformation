package com.gjc.controller;

import com.gjc.model.EntityType;
import com.gjc.model.HostHolder;
import com.gjc.service.LikeService;
import com.gjc.service.NewsService;
import com.gjc.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @RequestMapping(path={"/like"},method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(Model model, @RequestParam("newsId")int newsId){
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
        int count = newsService.updateLikeCount(newsId, (int) likeCount);
        System.out.println("like count = "+count);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path={"/dislike"},method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike(Model model, @RequestParam("newsId")int newsId){
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId, EntityType.ENTITY_NEWS, newsId);
        int count=newsService.updateLikeCount(newsId,(int)likeCount);
        System.out.println("dislike count="+count);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
