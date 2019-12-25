package com.gjc.async;

import java.util.List;

public interface EventHandler {
    //EventHandler需要处理哪些事件
    void doHandle(EventModel model);

    //EventHandler需要关心什么事件
    List<EventType> getSupportEventTypes();
}
