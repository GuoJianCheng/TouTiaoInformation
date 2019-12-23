package com.gjc.controller;

import com.gjc.model.HostHolder;
import com.gjc.model.Message;
import com.gjc.model.User;
import com.gjc.model.ViewObject;
import com.gjc.service.MessageService;
import com.gjc.service.UserService;
import com.gjc.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);


    @RequestMapping(path={"/msg/list"}, method = {RequestMethod.GET})
    public String getConversationList(Model model){
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<ViewObject>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for(Message msg : conversationList){
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                //从from_id和to_id中找出哪一个是当前用户的id
                int userId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(userId);
                vo.set("user", user);
                vo.set("unread", messageService.getConversationUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
        } catch (Exception e) {
            logger.error("获取站内信列表失败 "+e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path={"/msg/detail"}, method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @Param("conversationId") String conversationId){
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
            for(Message msg:conversationList){
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if(user == null)
                    continue;
                vo.set("heardUrl", user.getHeadUrl());
                vo.set("userId", user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);

        } catch (Exception e) {
            logger.error("获取详情消息失败 "+e.getMessage());
        }
        return "letterDetail";
    }


    @RequestMapping(path={"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        try {
            Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(message.getId());
        } catch (Exception e) {
            logger.error("添加消息失败 " + e.getMessage());
            return ToutiaoUtil.getJSONString(1,"插入消息失败");
        }
    }
}
