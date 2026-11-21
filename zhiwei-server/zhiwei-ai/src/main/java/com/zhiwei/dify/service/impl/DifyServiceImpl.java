package com.zhiwei.dify.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.zhiwei.common.exception.ServiceException;
import com.zhiwei.dify.config.DifyProperties;
import com.zhiwei.dify.domain.dto.DifyChatDto;
import com.zhiwei.dify.service.IDifyService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Dify 智能体对话服务实现类
 *
 * @author zhiwei
 */
@Slf4j
@Service
public class DifyServiceImpl implements IDifyService {

    /** SSE 打字机效果的最大事件数日志阈值 */
    private static final int MAX_LOG_ANSWER_LENGTH = 200;

    @Autowired
    private DifyProperties difyProperties;

    /** 用于异步读取 Dify 流式响应的线程池 */
    private ExecutorService streamExecutor;

    /** Dify HTTP 客户端 */
    private HttpClient httpClient;

    @PostConstruct
    public void init() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(difyProperties.getConnectTimeout()))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.streamExecutor = Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable, "dify-sse-thread");
            thread.setDaemon(true);
            return thread;
        });
        log.info("[Dify] 初始化完成, baseUrl={}, connectTimeout={}s, readTimeout={}s",
                difyProperties.getBaseUrl(), difyProperties.getConnectTimeout(), difyProperties.getReadTimeout());
    }

    @PreDestroy
    public void destroy() {
        if (streamExecutor != null) {
            streamExecutor.shutdownNow();
            log.info("[Dify] 流式线程池已关闭");
        }
    }

    @Override
    public SseEmitter chat(DifyChatDto chatDto, String user) {
        log.info("[Dify][chat] 收到对话请求, user={}, conversationId={}, query={}",
                user, chatDto.getConversationId(), chatDto.getQuery());
        // 0L 表示不设置超时时间，流式对话由 Dify 和连接状态控制结束
        SseEmitter emitter = new SseEmitter(0L);
        emitter.onCompletion(() -> log.info("[Dify][chat] SSE连接已完成, user={}", user));
        emitter.onTimeout(() -> log.warn("[Dify][chat] SSE连接超时, user={}", user));
        emitter.onError(e -> log.error("[Dify][chat] SSE连接异常, user={}", user, e));
        streamExecutor.execute(() -> doChat(chatDto, user, emitter));
        return emitter;
    }

    @Override
    public JSONObject listConversations(String user, String lastId, Integer limit) {
        int pageSize = normalizeLimit(limit);
        StringBuilder url = new StringBuilder(baseUrl())
                .append("/conversations?user=").append(encode(user))
                .append("&limit=").append(pageSize);
        if (StrUtil.isNotBlank(lastId)) {
            url.append("&last_id=").append(encode(lastId));
        }
        log.info("[Dify][conversations] 请求会话列表, user={}, lastId={}, limit={}", user, lastId, pageSize);
        String response = executeGet(url.toString());
        log.info("[Dify][conversations] 会话列表响应: {}", response);
        return JSON.parseObject(response);
    }

    @Override
    public JSONObject listMessages(String conversationId, String user, String firstId, Integer limit) {
        int pageSize = normalizeLimit(limit);
        StringBuilder url = new StringBuilder(baseUrl())
                .append("/messages?conversation_id=").append(encode(conversationId))
                .append("&user=").append(encode(user))
                .append("&limit=").append(pageSize);
        if (StrUtil.isNotBlank(firstId)) {
            url.append("&first_id=").append(encode(firstId));
        }
        log.info("[Dify][messages] 请求会话历史消息, conversationId={}, user={}, firstId={}, limit={}",
                conversationId, user, firstId, pageSize);
        String response = executeGet(url.toString());
        log.info("[Dify][messages] 会话历史消息响应: {}", response);
        return JSON.parseObject(response);
    }

    @Override
    public JSONObject deleteConversation(String conversationId, String user) {
        log.info("[Dify][delete] 请求删除会话, conversationId={}, user={}", conversationId, user);
        JSONObject body = new JSONObject();
        body.put("user", user);
        String url = baseUrl() + "/conversations/" + encode(conversationId);
        HttpRequest request = baseRequest(url)
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(body.toJSONString(), StandardCharsets.UTF_8))
                .build();
        String response = execute(request);
        log.info("[Dify][delete] 删除会话响应: {}", response);
        // Dify 删除成功返回 204 No Content，此处统一包装成前端易读结构
        if (StrUtil.isBlank(response)) {
            JSONObject result = new JSONObject();
            result.put("result", "success");
            return result;
        }
        return JSON.parseObject(response);
    }

    /**
     * 异步调用 Dify 流式对话接口，并把 SSE 数据转发给前端
     */
    private void doChat(DifyChatDto chatDto, String user, SseEmitter emitter) {
        long startTime = System.currentTimeMillis();
        try {
            String body = buildChatBody(chatDto, user);
            HttpRequest request = baseRequest(baseUrl() + "/chat-messages")
                    .header("Content-Type", "application/json")
                    .header("Accept", "text/event-stream")
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();
            log.info("[Dify][chat] 调用Dify流式对话接口, url={}, body={}", request.uri(), body);

            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            log.info("[Dify][chat] Dify响应状态码: {}", response.statusCode());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String errorBody = new String(response.body().readAllBytes(), StandardCharsets.UTF_8);
                log.error("[Dify][chat] Dify返回异常, status={}, body={}", response.statusCode(), errorBody);
                sendErrorEvent(emitter, "Dify接口调用失败(HTTP " + response.statusCode() + "): " + errorBody);
                return;
            }

            int eventCount = 0;
            String lastConversationId = null;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
                String line;
                String currentEvent = "message";
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty()) {
                        currentEvent = "message";
                        continue;
                    }
                    // SSE 注释行（心跳）直接忽略
                    if (line.startsWith(":")) {
                        continue;
                    }
                    if (line.startsWith("event:")) {
                        currentEvent = line.substring("event:".length()).trim();
                        continue;
                    }
                    if (!line.startsWith("data:")) {
                        continue;
                    }
                    String data = line.substring("data:".length()).trim();
                    if (StrUtil.isBlank(data)) {
                        continue;
                    }
                    eventCount++;
                    String conversationId = logSseData(currentEvent, data);
                    // 会话ID只在首次出现时记录，避免每个流式事件都打印日志
                    if (StrUtil.isNotBlank(conversationId) && !conversationId.equals(lastConversationId)) {
                        lastConversationId = conversationId;
                        log.info("[Dify][chat] 当前会话ID: {}", conversationId);
                    }
                    // 原样转发 Dify 的 SSE data 数据，前端根据 answer 字段持续输出
                    emitter.send(SseEmitter.event().data(data));
                }
            }
            log.info("[Dify][chat] 流式对话完成, user={}, 转发事件数={}, 耗时={}ms",
                    user, eventCount, System.currentTimeMillis() - startTime);
            emitter.complete();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[Dify][chat] 流式对话线程被中断, user={}", user, e);
            sendErrorEvent(emitter, "对话请求被中断，请稍后重试");
        } catch (Exception e) {
            log.error("[Dify][chat] 流式对话异常, user={}", user, e);
            sendErrorEvent(emitter, "对话请求异常: " + e.getMessage());
        }
    }

    /**
     * 组装调用 Dify 对话接口的请求体
     */
    private String buildChatBody(DifyChatDto chatDto, String user) {
        JSONObject body = new JSONObject();
        Map<String, Object> inputs = chatDto.getInputs() == null ? new HashMap<>() : chatDto.getInputs();
        body.put("inputs", inputs);
        body.put("query", chatDto.getQuery());
        // 必须使用 streaming 模式
        body.put("response_mode", "streaming");
        body.put("user", user);
        if (StrUtil.isNotBlank(chatDto.getConversationId())) {
            body.put("conversation_id", chatDto.getConversationId());
        }
        return body.toJSONString();
    }

    /**
     * 记录 Dify 流式数据关键字段，便于排查问题
     *
     * @return 数据中的会话ID
     */
    private String logSseData(String event, String data) {
        try {
            JSONObject json = JSON.parseObject(data);
            String answer = json.getString("answer");
            if (StrUtil.isNotBlank(answer)) {
                String printAnswer = answer.length() > MAX_LOG_ANSWER_LENGTH
                        ? answer.substring(0, MAX_LOG_ANSWER_LENGTH) + "..." : answer;
                log.debug("[Dify][chat] 事件={}, 增量内容={}", event, printAnswer);
            } else {
                log.debug("[Dify][chat] 事件={}, data={}", event, data);
            }
            return json.getString("conversation_id");
        } catch (Exception e) {
            log.warn("[Dify][chat] 解析SSE数据失败, event={}, data={}", event, data);
            return null;
        }
    }

    /**
     * 向前端发送错误事件
     */
    private void sendErrorEvent(SseEmitter emitter, String message) {
        JSONObject error = new JSONObject();
        error.put("event", "error");
        error.put("message", message);
        try {
            emitter.send(SseEmitter.event().name("error").data(error.toJSONString()));
        } catch (Exception e) {
            log.warn("[Dify][chat] 发送错误事件失败: {}", e.getMessage());
        } finally {
            try {
                emitter.complete();
            } catch (Exception ignored) {
                // 连接已关闭，无需处理
            }
        }
    }

    /**
     * 发送 GET 请求并返回响应体
     */
    private String executeGet(String url) {
        HttpRequest request = baseRequest(url).GET().build();
        return execute(request);
    }

    /**
     * 执行 HTTP 请求，统一处理状态码和异常
     */
    private String execute(HttpRequest request) {
        try {
            log.debug("[Dify] 发送HTTP请求: {} {}", request.method(), request.uri());
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            log.info("[Dify] 响应状态码: {}, url={}", response.statusCode(), request.uri());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.error("[Dify] 接口调用失败, status={}, body={}", response.statusCode(), response.body());
                throw new ServiceException("Dify接口调用失败(HTTP " + response.statusCode() + "): " + response.body());
            }
            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[Dify] 请求被中断, url={}", request.uri(), e);
            throw new ServiceException("Dify接口调用被中断");
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("[Dify] 请求异常, url={}", request.uri(), e);
            throw new ServiceException("Dify接口调用异常: " + e.getMessage());
        }
    }

    /**
     * 构建带鉴权信息的请求对象
     */
    private HttpRequest.Builder baseRequest(String url) {
        if (StrUtil.isBlank(difyProperties.getApiKey())) {
            throw new ServiceException("未配置Dify应用密钥，请检查 dify.api-key 配置");
        }
        return HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(difyProperties.getReadTimeout()))
                .header("Authorization", "Bearer " + difyProperties.getApiKey())
                .header("Accept", "application/json");
    }

    /**
     * 获取 Dify 服务地址（去掉末尾斜杠）
     */
    private String baseUrl() {
        String baseUrl = difyProperties.getBaseUrl();
        if (StrUtil.isBlank(baseUrl)) {
            throw new ServiceException("未配置Dify服务地址，请检查 dify.base-url 配置");
        }
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    /**
     * 规范化分页数量，Dify 最大支持 100
     */
    private int normalizeLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return 100;
        }
        return Math.min(limit, 100);
    }

    /**
     * URL 参数编码
     */
    private String encode(String value) {
        return URLEncoder.encode(StrUtil.blankToDefault(value, ""), StandardCharsets.UTF_8);
    }
}
