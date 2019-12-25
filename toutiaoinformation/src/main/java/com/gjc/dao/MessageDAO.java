package com.gjc.dao;

import com.gjc.model.Comment;
import com.gjc.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String TABLE_INSERT_FIELDS = " m.from_id, m.to_id, m.content, m.created_date, m.has_read, m.conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId}, #{toId}, #{content}, #{createdDate}, #{hasRead}, #{conversationId})"})
    int addMessage(Message message);

    @Select({"select", SELECT_FIELDS, "from",
            TABLE_NAME, "where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);

    /*select m.* ,t.cnt
    from message m ,(select MAX(id) as id, COUNT(conversation_id) as cnt from message where from_id=17 or to_id=17 group by conversation_id) t
    where m.id = t.id
    order by created_date desc
    limit offset, limit;*/
    @Select({" select ", TABLE_INSERT_FIELDS, ", t.cnt as id ",
    " from ", TABLE_NAME, " m, ",
            "(select MAX(id) as id, COUNT(conversation_id) as cnt from ",
            TABLE_NAME, "where from_id=#{userId} or to_id=#{userId}", "group by conversation_id) t",
            "where m.id = t.id order by created_date desc limit #{offset}, #{limit} "})
    List<Message> getConversationList(@Param("userId") int userId,
                                        @Param("offset") int offset, @Param("limit") int limit);

    //这里是to_Id = #{userId}，from_id没有意义的，发给当前用户（我）才有意义
    @Select({" select count(id) from ",TABLE_NAME, "where has_read = 0 and to_id = #{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    //看了站内信，点到letterDetail中，就读过了，将has_read置为1
    @Update({"update", TABLE_NAME, "set has_read = 1 where to_id = #{userId} and conversation_id=#{conversationId}"})
    void updateHasRead(@Param("userId") int userId, @Param("conversationId") String conversationId);
}
