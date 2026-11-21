package com.zhiwei.park.domain;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwei.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.zhiwei.common.core.domain.BaseEntity;

/**
 * 入驻表对象 settle_in
 * 
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "入驻表对象")
public class SettleIn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(title = "主键ID")
    private Long id;

    /** 企业名称 */
    @Schema(title = "企业名称")
    @Excel(name = "企业名称")
    private String tenantName;

    /** 企业ID */
    @Schema(title = "企业ID")
    @Excel(name = "企业ID")
    private Long tenantId;

    /** 统一社会信用代码 */
    @Schema(title = "统一社会信用代码")
    @Excel(name = "统一社会信用代码")
    private String creditCode;

    /** 入驻开始时间 */
    @Schema(title = "入驻开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "入驻开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime startDate;

    /** 入驻结束时间 */
    @Schema(title = "入驻结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "入驻结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime endDate;

    /** 服务等级名称 */
    @Schema(title = "服务等级名称")
    @Excel(name = "服务等级名称")
    private String serviceLevelName;

    /** 入驻点位 */
    @Schema(title = "入驻点位")
    @Excel(name = "入驻点位")
    private String pointCode;

    /** 状态 (0: 已入驻, 1: 已退住) */
    @Schema(title = "状态 (0: 已入驻, 1: 已退住)")
    @Excel(name = "状态 (0: 已入驻, 1: 已退住)")
    private Integer status;

    /** 排序编号 */
    @Schema(title = "排序编号")
    @Excel(name = "排序编号")
    private Integer sortOrder;

}
