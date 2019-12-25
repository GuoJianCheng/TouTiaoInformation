package com.gjc.async.handler;

import com.gjc.async.EventHandler;
import com.gjc.async.EventModel;
import com.gjc.async.EventType;
import com.gjc.model.Message;
import com.gjc.service.MessageService;
import com.gjc.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.*;

@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        //判断是否有异常登录，如果有发邮件或者站内信
        Message message = new Message();
        int fromId = model.getActorId();
        int toId = model.getActorId();
        String conversationId = (fromId < toId) ? String.valueOf(fromId)+"_"+ String.valueOf(toId) : String.valueOf(toId)+"_"+ String.valueOf(fromId);
        message.setFromId(fromId);
        message.setToId(toId);
        message.setContent("你上次的登录ip异常");
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setConversationId(conversationId);
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExt("username"));

        mailSender.sendWithHTMLTemplate(model.getExt("email"),"登录异常","mails/welcome.html",map);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
