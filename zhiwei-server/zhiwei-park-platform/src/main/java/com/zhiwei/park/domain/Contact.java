package com.zhiwei.park.domain;

import com.zhiwei.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.zhiwei.common.core.domain.BaseEntity;

/**
 * 企业联系人对象 contact
 * 
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "企业联系人对象")
public class Contact extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(title = "主键")
    private Long id;

    /** 手机号 */
    @Schema(title = "手机号")
    @Excel(name = "手机号")
    private String phone;

    /** 名称 */
    @Schema(title = "名称")
    @Excel(name = "名称")
    private String name;

    /** 头像 */
    @Schema(title = "头像")
    @Excel(name = "头像")
    private String avatar;

    /** OpenID */
    @Schema(title = "OpenID")
    @Excel(name = "OpenID")
    private String openId;

    /** 性别(0:男，1:女) */
    @Schema(title = "性别(0:男，1:女)")
    @Excel(name = "性别(0:男，1:女)")
    private Integer gender;

}
