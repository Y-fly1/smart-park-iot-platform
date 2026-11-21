package com.zhiwei.park.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMiniAppProperties
{
    /** 小程序 appid */
    private String appid;

    /** 小程序 appsecret */
    private String secret;
}
