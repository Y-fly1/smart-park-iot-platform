package com.zhiwei.dify.controller;

import cn.hutool.core.util.StrUtil;
import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.common.exception.ServiceException;
import com.zhiwei.common.utils.SecurityUtils;
import com.zhiwei.dify.config.DifyProperties;
import com.zhiwei.dify.domain.dto.DifyChatDto;
import com.zhiwei.dify.service.IDifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Dify 智能体对话接口
 *
 * @author zhiwei
 */
@Slf4j
@RestController
@RequestMapping("/ai/dify")
@Tag(name = "Dify智能体对话接口")
public class DifyController extends BaseController {

    @Autowired
    private IDifyService difyService;

    @Autowired
    private DifyProperties difyProperties;

    /**
     * 发送对话消息（流式输出）
     */
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "发送对话消息（流式输出）")
    public SseEmitter chat(@RequestBody DifyChatDto chatDto) {
        log.info("[Dify][controller] 收到发送消息请求: query={}, conversationId={}, user={}",
                chatDto.getQuery(), chatDto.getConversationId(), chatDto.getUser());
        if (StrUtil.isBlank(chatDto.getQuery())) {
            throw new ServiceException("对话内容不能为空");
        }
        String user = resolveUser(chatDto.getUser());
        log.info("[Dify][controller] 发送消息使用用户标识: {}", user);
        return difyService.chat(chatDto, user);
    }

    /**
     * 获取会话列表
     */
    @GetMapping("/conversations")
    @Operation(summary = "获取会话列表")
    public AjaxResult conversations(@RequestParam(required = false) String user,
                                    @RequestParam(required = false) String lastId,
                                    @RequestParam(required = false) Integer limit) {
        log.info("[Dify][controller] 收到获取会话列表请求: user={}, lastId={}, limit={}", user, lastId, limit);
        String resolvedUser = resolveUser(user);
        return success(difyService.listConversations(resolvedUser, lastId, limit));
    }

    /**
     * 获取会话历史消息
     */
    @GetMapping("/messages")
    @Operation(summary = "获取会话历史消息")
    public AjaxResult messages(@RequestParam String conversationId,
                               @RequestParam(required = false) String user,
                               @RequestParam(required = false) String firstId,
                               @RequestParam(required = false) Integer limit) {
        log.info("[Dify][controller] 收到获取历史消息请求: conversationId={}, user={}, firstId={}, limit={}",
                conversationId, user, firstId, limit);
        if (StrUtil.isBlank(conversationId)) {
            throw new ServiceException("会话ID不能为空");
        }
        String resolvedUser = resolveUser(user);
        return success(difyService.listMessages(conversationId, resolvedUser, firstId, limit));
    }

    /**
     * 删除会话
     */
    @DeleteMapping("/conversations/{conversationId}")
    @Operation(summary = "删除会话")
    public AjaxResult deleteConversation(@PathVariable String conversationId,
                                         @RequestParam(required = false) String user) {
        log.info("[Dify][controller] 收到删除会话请求: conversationId={}, user={}", conversationId, user);
        if (StrUtil.isBlank(conversationId)) {
            throw new ServiceException("会话ID不能为空");
        }
        String resolvedUser = resolveUser(user);
        return success(difyService.deleteConversation(conversationId, resolvedUser));
    }

    /**
     * 解析用户标识：优先使用登录用户ID，未登录时使用请求参数，最后使用默认值
     */
    private String resolveUser(String requestUser) {
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId != null) {
                log.debug("[Dify] 使用登录用户ID作为Dify用户标识: {}", userId);
                return String.valueOf(userId);
            }
        } catch (Exception e) {
            log.warn("[Dify] 未获取到登录用户信息，使用请求参数中的用户标识: {}", requestUser);
        }
        String user = StrUtil.blankToDefault(requestUser, difyProperties.getDefaultUser());
        log.info("[Dify] 当前Dify用户标识: {}", user);
        return user;
    }
}
