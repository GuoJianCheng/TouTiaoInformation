package com.gjc.controller;

import com.gjc.model.EntityType;
import com.gjc.model.HostHolder;
import com.gjc.model.News;
import com.gjc.model.ViewObject;
import com.gjc.service.LikeService;
import com.gjc.service.NewsService;
import com.gjc.service.UserService;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.swing.text.View;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    private List<ViewObject>getNews(int userId,int offset,int limit){
        int localUserId = hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
        List<News> newsList = newsService.getLatestNews(userId,offset,limit);
        List<ViewObject> vos = new ArrayList<>();
        for(News news: newsList){
            ViewObject vo = new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));

            if(localUserId != 0){
                //登录状态
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
            }else{
                vo.set("like",0);
            }


            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path={"/","/index"},method={RequestMethod.GET,RequestMethod.POST})
    public String index(Model model,@RequestParam(value="pop", defaultValue = "0") int pop){
        model.addAttribute("vos",getNews(0,0,10));
        System.out.println("pop="+pop);
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path={"/user/{userId}/"},method={RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId){
        model.addAttribute("vos",getNews(userId,0,10));
        return "home";
    }
}
