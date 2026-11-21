package com.zhiwei.park.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 企业基本信息VO（入驻字段填充）
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "企业基本信息")
public class TenantBasicInfoVo
{
    /** 企业名称 */
    @Schema(title = "企业名称")
    private String name;

    /** 统一社会信用代码 */
    @Schema(title = "统一社会信用代码")
    private String creditCode;

    /** 联系电话 */
    @Schema(title = "联系电话")
    private String phone;

    /** 所属片区 */
    @Schema(title = "所属片区")
    private String nation;

    /** 文化程度 */
    @Schema(title = "文化程度")
    private String educationLevel;

    /** 法人代表号 */
    @Schema(title = "法人代表号")
    private String socialSecurityCard;

    /** 经营状态 */
    @Schema(title = "经营状态")
    private String livingSituation;

    /** 主要用能 */
    @Schema(title = "主要用能")
    private String religiousBelief;

    /** 用能结构 */
    @Schema(title = "用能结构")
    private String economicSource;

    /** 企业类型 */
    @Schema(title = "企业类型")
    private String maritalStatus;

    /** 医疗费用支付方式 */
    @Schema(title = "医疗费用支付方式")
    private String medicalPaymentMethod;

    /** 核心建议，0-不建议入驻，1-建议入驻 */
    @Schema(title = "核心建议，0-不建议入驻，1-建议入驻")
    private Integer coreSuggestion;
}
