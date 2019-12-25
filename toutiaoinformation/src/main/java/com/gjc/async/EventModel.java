package com.gjc.async;

import java.util.HashMap;
import java.util.Map;

/**
 * EnventModel表示刚刚发生的是什么事情
 */
public class EventModel {
    //事情的类型
    private EventType type;
    //事件的触发者
    private int actorId;
    //事件的触发对象
    private int entityType;
    private int entityId;
    //触发对象的拥有者
    private int entityOwnerId;

    //现场的参数或数据需要保存下来，以后会用
    private Map<String, String> exts = new HashMap<String, String>();

    public String getExt(String key){
        return exts.get(key);
    }
    public EventModel setExt(String key, String value){
        exts.put(key, value);
        return this;
    }

    public EventModel(){

    }

    public EventModel(EventType type){
        this.type = type;
    }


    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
