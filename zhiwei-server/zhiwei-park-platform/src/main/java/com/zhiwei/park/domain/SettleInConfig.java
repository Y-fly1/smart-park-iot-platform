package com.zhiwei.park.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwei.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.zhiwei.common.core.domain.BaseEntity;

/**
 * 配置表对象 settle_in_config
 * 
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "配置表对象")
public class SettleInConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(title = "主键ID")
    private Long id;

    /** 入驻表ID */
    @Schema(title = "入驻表ID")
    @Excel(name = "入驻表ID")
    private Long settleInId;

    /** 服务等级ID */
    @Schema(title = "服务等级ID")
    @Excel(name = "服务等级ID")
    private Long serviceLevelId;

    /** 服务等级名称 */
    @Schema(title = "服务等级名称")
    @Excel(name = "服务等级名称")
    private String serviceLevelName;

    /** 费用开始时间 */
    @Schema(title = "费用开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "费用开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime feeStartDate;

    /** 费用结束时间 */
    @Schema(title = "费用结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "费用结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime feeEndDate;

    /** 押金（元） */
    @Schema(title = "押金（元）")
    @Excel(name = "押金", readConverterExp = "元=")
    private BigDecimal deposit;

    /** 服务费用（元/月） */
    @Schema(title = "服务费用（元/月）")
    @Excel(name = "服务费用", readConverterExp = "元=/月")
    private BigDecimal serviceFee;

    /** 场地费用（元/月） */
    @Schema(title = "场地费用（元/月）")
    @Excel(name = "场地费用", readConverterExp = "元=/月")
    private BigDecimal siteFee;

    /** 医保支付（元/月） */
    @Schema(title = "医保支付（元/月）")
    @Excel(name = "医保支付", readConverterExp = "元=/月")
    private BigDecimal insurancePayment;

    /** 政府补贴（元/月） */
    @Schema(title = "政府补贴（元/月）")
    @Excel(name = "政府补贴", readConverterExp = "元=/月")
    private BigDecimal governmentSubsidy;

    /** 其他费用（元/月） */
    @Schema(title = "其他费用（元/月）")
    @Excel(name = "其他费用", readConverterExp = "元=/月")
    private BigDecimal otherFees;

    /** 排序编号 */
    @Schema(title = "排序编号")
    @Excel(name = "排序编号")
    private Integer sortOrder;

}
