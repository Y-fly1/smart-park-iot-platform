package com.zhiwei.park.domain;

import com.zhiwei.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.zhiwei.common.core.domain.BaseEntity;

/**
 * 企业表对象 tenant
 * 
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "企业表对象")
public class Tenant extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Schema(title = "id")
    private Long id;

    /** 名称 */
    @Schema(title = "名称")
    @Excel(name = "名称")
    private String name;

    /** 头像 */
    @Schema(title = "头像")
    @Excel(name = "头像")
    private String image;

    /** 统一社会信用代码 */
    @Schema(title = "统一社会信用代码")
    @Excel(name = "统一社会信用代码")
    private String creditCode;

    /** 性别（0:女  1:男） */
    @Schema(title = "性别（0:女  1:男）")
    @Excel(name = "性别", readConverterExp = "0=:女,1=:男")
    private Integer sex;

    /** 状态（0:禁用，1:已入驻 2:请假 3:已退住） */
    @Schema(title = "状态（0:禁用，1:已入驻 2:请假 3:已退住）")
    @Excel(name = "状态", readConverterExp = "0=:禁用，1:已入驻,2=:请假,3=:已退住")
    private Integer status;

    /** 手机号 */
    @Schema(title = "手机号")
    @Excel(name = "手机号")
    private String phone;

    /** 出生日期 */
    @Schema(title = "出生日期")
    @Excel(name = "出生日期")
    private String birthday;

    /** 家庭住址 */
    @Schema(title = "家庭住址")
    @Excel(name = "家庭住址")
    private String address;

    /** 信用代码国徽面 */
    @Schema(title = "信用代码国徽面")
    @Excel(name = "信用代码国徽面")
    private String licenseCopyImg;

    /** 信用代码人像面 */
    @Schema(title = "信用代码人像面")
    @Excel(name = "信用代码人像面")
    private String licenseImg;

    /** 点位编号 */
    @Schema(title = "点位编号")
    @Excel(name = "点位编号")
    private String pointCode;

    /** 点位id */
    @Schema(title = "点位id")
    @Excel(name = "点位id")
    private Long pointId;

    /** 所属片区 */
    @Schema(title = "所属片区")
    @Excel(name = "所属片区")
    private String nation;

    /** 文化程度 */
    @Schema(title = "文化程度")
    @Excel(name = "文化程度")
    private String educationLevel;

    /** 法人代表号 */
    @Schema(title = "法人代表号")
    @Excel(name = "法人代表号")
    private String socialSecurityCard;

    /** 经营状态 */
    @Schema(title = "经营状态")
    @Excel(name = "经营状态")
    private String livingSituation;

    /** 主要用能 */
    @Schema(title = "主要用能")
    @Excel(name = "主要用能")
    private String religiousBelief;

    /** 用能结构 */
    @Schema(title = "用能结构")
    @Excel(name = "用能结构")
    private String economicSource;

    /** 企业类型 */
    @Schema(title = "企业类型")
    @Excel(name = "企业类型")
    private String maritalStatus;

    /** 医疗费用支付方式 */
    @Schema(title = "医疗费用支付方式")
    @Excel(name = "医疗费用支付方式")
    private String medicalPaymentMethod;

    /** 核心建议 */
    @Schema(title = "核心建议")
    @Excel(name = "核心建议")
    private String coreSuggestion;

}
