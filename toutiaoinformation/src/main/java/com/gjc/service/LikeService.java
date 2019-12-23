package com.gjc.service;

import com.gjc.util.JedisAdapter;
import com.gjc.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    //判断某个用户对某一项元素(可以是资讯，也可以是评论)是否喜欢
    //如果喜欢返回1，不喜欢返回-1，否则返回0
    public int getLikeStatus(int userId, int entityType, int entityId){
       String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
       if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
           return 1;
       }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId)) ? -1 : 0;
    }

    public long like(int userId, int entityType, int entityId){
        //把当前用户加到喜欢的集合里
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        //再把当前用户从不喜欢的集合里删除掉
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId, int entityType, int entityId){
        //把当前用户加到不喜欢的集合里
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));
        //再把当前用户从喜欢的集合里删除掉
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.srem(likeKey,String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }
}
