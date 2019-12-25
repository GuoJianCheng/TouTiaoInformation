package com.gjc.util;


/**
 * 统一的规范管理redis中的key
 * 这里是业务+key
 */
public class RedisKeyUtil {
    private static String SPLIT=":";
    private static String BIZ_LIKE="LIKE";
    private static String BIZ_DISLIKE="DISLIKE";
    private static String BIZ_EVENT="EVENT";

    public static String getEventQueueKey(){
        return BIZ_EVENT;
    }

    public static String getLikeKey(int entityId, int entityType){
        return BIZ_LIKE+SPLIT+String.valueOf(entityType)+SPLIT +String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityId, int entityType){
        return BIZ_DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT +String.valueOf(entityId);
    }
}
