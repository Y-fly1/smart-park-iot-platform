package com.zhiwei.park.util;

import com.zhiwei.common.constant.Constants;
import com.zhiwei.common.exception.ServiceException;
import com.zhiwei.common.utils.ServletUtils;
import com.zhiwei.common.utils.StringUtils;
import com.zhiwei.framework.web.service.TokenService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 小程序用户身份解析工具
 *
 * @author ypf
 * @date 2026-10-30
 */
@Component
public class MemberAuthHelper
{
    /** 令牌自定义标识 */
    @Value("${token.header}")
    private String header;

    @Autowired
    private TokenService tokenService;

    /**
     * 从请求头令牌中获取当前联系人id
     *
     * @return 联系人id
     */
    public Long getUserId()
    {
        String token = ServletUtils.getRequest().getHeader(header);
        if (StringUtils.isEmpty(token))
        {
            throw new ServiceException("请先登录");
        }
        if (token.startsWith(Constants.TOKEN_PREFIX))
        {
            token = token.substring(Constants.TOKEN_PREFIX.length());
        }
        try
        {
            Claims claims = tokenService.parseToken(token);
            Object userId = claims.get("userId");
            if (userId == null)
            {
                throw new ServiceException("登录已失效，请重新登录");
            }
            return ((Number) userId).longValue();
        }
        catch (ServiceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new ServiceException("登录已失效，请重新登录");
        }
    }
}
