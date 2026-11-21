package com.zhiwei.park.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhiwei.common.core.redis.RedisCache;
import com.zhiwei.common.exception.ServiceException;
import com.zhiwei.park.config.WxMiniAppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 微信小程序接口调用工具
 *
 * @author ypf
 * @date 2026-10-30
 */
@Component
public class WxMiniAppClient
{
    /** 登录凭证校验接口 */
    private static final String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /** 获取接口调用凭证接口 */
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    /** 获取手机号接口 */
    private static final String PHONE_NUMBER_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";

    /** access_token 缓存前缀 */
    private static final String ACCESS_TOKEN_CACHE_KEY = "wx:miniapp:access_token:";

    /** access_token 提前过期秒数，避免临界失效 */
    private static final int EXPIRE_AHEAD_SECONDS = 300;

    @Autowired
    private WxMiniAppProperties properties;

    @Autowired
    private RedisCache redisCache;

    /**
     * 用登录凭证code换取openid
     *
     * @param code 前端传入的临时登录凭证
     * @return openid
     */
    public String code2Session(String code)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("appid", properties.getAppid());
        params.put("secret", properties.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        JSONObject result = JSONUtil.parseObj(HttpUtil.get(CODE2SESSION_URL, params));
        checkError(result);
        String openId = result.getStr("openid");
        if (StrUtil.isBlank(openId))
        {
            throw new ServiceException("获取微信openid失败");
        }
        return openId;
    }

    /**
     * 用手机号凭证换取手机号
     *
     * @param phoneCode 前端传入的手机号凭证
     * @return 手机号
     */
    public String getPhoneNumber(String phoneCode)
    {
        String url = PHONE_NUMBER_URL + "?access_token=" + getAccessToken();
        JSONObject body = JSONUtil.createObj().set("code", phoneCode);
        JSONObject result = JSONUtil.parseObj(HttpUtil.post(url, body.toString()));
        checkError(result);
        JSONObject phoneInfo = result.getJSONObject("phone_info");
        String phone = phoneInfo == null ? null : phoneInfo.getStr("phoneNumber");
        if (StrUtil.isBlank(phone))
        {
            throw new ServiceException("获取微信手机号失败");
        }
        return phone;
    }

    /**
     * 获取接口调用凭证，优先取缓存
     *
     * @return access_token
     */
    private String getAccessToken()
    {
        String cacheKey = ACCESS_TOKEN_CACHE_KEY + properties.getAppid();
        Object cached = redisCache.getCacheObject(cacheKey);
        if (cached != null && StrUtil.isNotBlank(cached.toString()))
        {
            return cached.toString();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", properties.getAppid());
        params.put("secret", properties.getSecret());
        JSONObject result = JSONUtil.parseObj(HttpUtil.get(ACCESS_TOKEN_URL, params));
        checkError(result);
        String accessToken = result.getStr("access_token");
        if (StrUtil.isBlank(accessToken))
        {
            throw new ServiceException("获取微信access_token失败");
        }
        int expiresIn = result.getInt("expires_in", 7200);
        redisCache.setCacheObject(cacheKey, accessToken,
                Math.max(expiresIn - EXPIRE_AHEAD_SECONDS, 60), TimeUnit.SECONDS);
        return accessToken;
    }

    /**
     * 校验微信接口返回的错误码
     *
     * @param result 微信接口返回结果
     */
    private void checkError(JSONObject result)
    {
        int errCode = result.getInt("errcode", 0);
        if (errCode != 0)
        {
            throw new ServiceException("微信接口调用失败：" + result.getStr("errmsg", "未知错误"));
        }
    }
}
