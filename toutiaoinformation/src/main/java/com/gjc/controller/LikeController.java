package com.gjc.controller;

import com.gjc.async.EventModel;
import com.gjc.async.EventProducer;
import com.gjc.async.EventType;
import com.gjc.model.EntityType;
import com.gjc.model.HostHolder;
import com.gjc.model.News;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path={"/like"},method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(Model model, @RequestParam("newsId")int newsId) {
        int userId = hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
        if(userId==0){
            return ToutiaoUtil.getJSONString(1,"未登录点赞");
        }

        long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
        News news = newsService.getById(newsId);
        newsService.updateLikeCount(newsId, (int) likeCount);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
        .setActorId(userId).setEntityId(newsId)
                .setEntityType(EntityType.ENTITY_NEWS).setEntityOwnerId(news.getUserId()));

        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path={"/dislike"},method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike(Model model, @RequestParam("newsId")int newsId) {
        int userId = hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
        if(userId==0){
            return ToutiaoUtil.getJSONString(1,"未登录点踩");
        }
        long likeCount = likeService.disLike(userId, EntityType.ENTITY_NEWS, newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
