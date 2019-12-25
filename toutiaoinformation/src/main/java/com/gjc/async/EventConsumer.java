package com.gjc.async;

import com.alibaba.fastjson.JSON;
import com.gjc.util.JedisAdapter;
import com.gjc.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();

    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {

        //将所有实现了EventHandler接口的Handler都找出来
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if(beans != null){
            for(Map.Entry<String, EventHandler> entry:beans.entrySet()){
                //每一个Handler都会关注某些事件eventTypes
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for(EventType type : eventTypes){
                    //将这些事件的类型加入到config中
                    if(!config.containsKey(type)){
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    //反向的，根据关心的事件类型，将Handler自己加入进去
                    config.get(type).add(entry.getValue());
                }
            }
        }
        // 启动线程去消费事件
        Thread thread = new Thread(new Runnable(){

            @Override
            public void run() {
                while(true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    //Redis Brpop 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
                    //返回值：假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。
                    // 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
                    List<String>events = jedisAdapter.brpop(0,key);
                    for(String message:events){
                        // 第一个元素是队列名字
                        if(message.equals(key)){
                            continue;
                        }

                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
                        if(!config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件");
                            continue;
                        }
                        for(EventHandler handler : config.get(eventModel.getType())){
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
