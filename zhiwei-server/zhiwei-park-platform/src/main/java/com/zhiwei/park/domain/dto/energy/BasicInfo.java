package com.zhiwei.park.domain.dto.energy;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 基本信息
 */
@Data
@Schema(description = "基本信息")
public class BasicInfo {


    /**
     * 企业名称
     */
    @JsonProperty("tenantName")
    @Schema(title = "企业名称")
    private String tenantName;

    /**
     *  统一社会信用代码码
     */
    @JsonProperty("creditCode")
    @Schema(title = "统一社会信用代码码")
    private String creditCode;

    /**
     * 出生日期
     */
    @JsonProperty("birthDate")
    @Schema(title = "出生日期")
    private String birthDate;

    /**
     * 年龄
     */
    @JsonProperty("age")
    @Schema(title = "年龄")
    private Integer age;

    /**
     * 性别
     */
    @JsonProperty("gender")
    @Schema(title = "性别")
    private String gender;

    /**
     * 所属片区
     */
    @JsonProperty("nation")
    @Schema(title = "所属片区")
    private String nation;

    /**
     * 法人代表号
     */
    @JsonProperty("socialSecurityCard")
    @Schema(title = "法人代表号")
    private String socialSecurityCard;

    /**
     * 主要用能
     */
    @JsonProperty("religiousBelief")
    @Schema(title = "主要用能")
    private String religiousBelief;

    /**
     * 文化程度
     */
    @JsonProperty("educationLevel")
    @Schema(title = "文化程度")
    private String educationLevel;

    /**
     * 企业类型
     */
    @JsonProperty("maritalStatus")
    @Schema(title = "企业类型")
    private String maritalStatus;

    /**
     * 经营状态
     */
    @JsonProperty("livingSituation")
    @Schema(title = "经营状态")
    private String livingSituation;

    /**
     * 医疗付款方式
     */
    @JsonProperty("medicalPaymentMethod")
    @Schema(title = "医疗付款方式")
    private String medicalPaymentMethod;

    /**
     * 用能结构
     */
    @JsonProperty("economicSource")
    @Schema(title = "用能结构")
    private String economicSource;

    /**
     * 企业联系方式
     */
    @JsonProperty("tenantContact")
    @Schema(title = "企业联系方式")
    private String tenantContact;

    /**
     * 信息提供者姓名
     */
    @JsonProperty("providerName")
    @Schema(title = "信息提供者姓名")
    private String providerName;

    /**
     * 信息提供者联系方式
     */
    @JsonProperty("providerContact")
    @Schema(title = "信息提供者联系方式")
    private String providerContact;

    /**
     * 与企业关系
     */
    @JsonProperty("relationshipWithTenant")
    @Schema(title = "与企业关系")
    private String relationshipWithTenant;

    /**
     * 知情同意书URL
     */
    @JsonProperty("informedConsentUrl")
    @Schema(title = "知情同意书URL")
    private String informedConsentUrl;
}