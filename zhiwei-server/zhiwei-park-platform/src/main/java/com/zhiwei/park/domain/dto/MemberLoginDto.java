package com.zhiwei.park.domain.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 小程序登录入参
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "小程序登录入参")
public class MemberLoginDto
{
    /** 临时登录凭证code */
    @NotBlank(message = "登录凭证不能为空")
    @Schema(title = "临时登录凭证code")
    private String code;

    /** 微信用户昵称 */
    @Schema(title = "微信用户昵称")
    private String nickName;

    /** 获取手机的临时code */
    @NotBlank(message = "手机号凭证不能为空")
    @Schema(title = "获取手机的临时code")
    private String phoneCode;
}
