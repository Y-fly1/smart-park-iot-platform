package com.zhiwei.service;
import com.zhiwei.domain.vo.MessageVO;
import java.util.List;

/**
 * 会话历史处理接口类

 */
public interface ChatHistoryService {


    /**
     * 保存用户的会话ID
     * @param userId
     * @param chatId
     */
    void saveHistory(String userId, String chatId);

    /**
     * 获取历史会话ID
     * @return
     */
    List<String> findChatIdList(String userId);

    /**
     * 获取聊天记录详情
     * @param chatId
     * @return
     */
    List<MessageVO> findChatHistoryDetails(String chatId);

    /**
     * 删除会话及聊天信息
     * @param chatId
     */
    void deleteChatHistory(String chatId, String userId);
}
