package com.zhiwei.dify.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Dify 智能体配置
 *
 * @author zhiwei
 */
@Data
@Component
@ConfigurationProperties(prefix = "dify")
public class DifyProperties {

    /** Dify 服务地址，例如 http://localhost/v1 */
    private String baseUrl = "http://localhost/v1";

    /** Dify 应用密钥 */
    private String apiKey;

    /** 连接超时时间（秒） */
    private Integer connectTimeout = 10;

    /** 请求超时时间（秒），流式对话建议设置较大值 */
    private Integer readTimeout = 300;

    /** 默认用户标识，未登录时使用 */
    private String defaultUser = "zhiwei-anonymous";
}
