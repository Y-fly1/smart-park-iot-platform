package com.zhiwei.park.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 小程序登录返回
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "小程序登录返回")
public class MemberLoginVo
{
    /** 令牌 */
    @Schema(title = "令牌")
    private String token;

    /** 昵称 */
    @Schema(title = "昵称")
    private String nickName;
}
