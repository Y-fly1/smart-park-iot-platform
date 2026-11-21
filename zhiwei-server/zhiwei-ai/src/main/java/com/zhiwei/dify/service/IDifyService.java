package com.zhiwei.dify.service;

import com.alibaba.fastjson2.JSONObject;
import com.zhiwei.dify.domain.dto.DifyChatDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Dify 智能体对话服务接口
 *
 * @author zhiwei
 */
public interface IDifyService {

    /**
     * 发送对话消息（流式输出）
     *
     * @param chatDto 对话参数
     * @param user    用户标识
     * @return SSE 发射器
     */
    SseEmitter chat(DifyChatDto chatDto, String user);

    /**
     * 获取会话列表
     *
     * @param user   用户标识
     * @param lastId 上一页最后一条记录ID（可选）
     * @param limit  每页数量
     * @return Dify 原始响应
     */
    JSONObject listConversations(String user, String lastId, Integer limit);

    /**
     * 获取会话历史消息
     *
     * @param conversationId 会话ID
     * @param user           用户标识
     * @param firstId        上一页第一条记录ID（可选）
     * @param limit          每页数量
     * @return Dify 原始响应
     */
    JSONObject listMessages(String conversationId, String user, String firstId, Integer limit);

    /**
     * 删除会话
     *
     * @param conversationId 会话ID
     * @param user           用户标识
     * @return Dify 原始响应
     */
    JSONObject deleteConversation(String conversationId, String user);
}
