package com.gjc.async.handler;

import com.gjc.async.EventHandler;
import com.gjc.async.EventModel;
import com.gjc.async.EventType;
import com.gjc.model.Message;
import com.gjc.model.User;
import com.gjc.service.MessageService;
import com.gjc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        //以后要是发生了点赞的事件就会来处理
        System.out.println("Liked");
        int fromId = model.getActorId();
        int toId = model.getEntityOwnerId();
        String conversationId = (fromId < toId) ? String.valueOf(fromId)+"_"+ String.valueOf(toId) : String.valueOf(toId)+"_"+ String.valueOf(fromId);
        Message message = new Message();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setConversationId(conversationId);
        message.setHasRead(0);
        User user = userService.getUser(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你的资讯，http://127.0.0.1:8080/news/"+model.getEntityId());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);


    }

    @Override
    public List<EventType> getSupportEventTypes() {
        System.out.println(Arrays.asList(EventType.LIKE));
        //只关心点赞的事件
        return Arrays.asList(EventType.LIKE);
    }
}
