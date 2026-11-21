package com.zhiwei.dify.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * Dify 对话请求参数
 *
 * @author zhiwei
 */
@Data
@Schema(description = "Dify对话请求参数")
public class DifyChatDto {

    /** 用户提问内容 */
    @Schema(title = "用户提问内容")
    private String query;

    /** 会话ID，为空表示开启新会话（由Dify生成并返回） */
    @Schema(title = "会话ID，为空表示新会话")
    private String conversationId;

    /** 用户标识，Dify要求同一应用内唯一 */
    @Schema(title = "用户标识")
    private String user;

    /** 应用输入变量，Agent应用默认传空对象 */
    @Schema(title = "应用输入变量")
    private Map<String, Object> inputs;
}
