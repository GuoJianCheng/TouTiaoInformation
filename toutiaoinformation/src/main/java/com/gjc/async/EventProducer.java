package com.gjc.async;

import com.alibaba.fastjson.JSONObject;
import com.gjc.util.JedisAdapter;
import com.gjc.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;
    //将事件推到队列里面去
    public boolean fireEvent(EventModel model){
        try {
            //将事件序列化
            String json = JSONObject.toJSONString(model);
            //获取队列的key
            String key = RedisKeyUtil.getEventQueueKey();
            //将序列化之后的事件放到队列里去
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
