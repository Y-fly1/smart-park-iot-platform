package com.zhiwei.park.domain;

import com.zhiwei.common.annotation.Excel;
import com.zhiwei.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 点位对象 point
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Data
@Schema(description = "点位实体")
public class Point extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 点位ID */
    @Schema(title = "点位ID")
    private Long id;

    /** 点位编号 */
    @Excel(name = "点位编号")
    @Schema(title = "点位编号")
    private String pointCode;

    /** 点位状态: 未入驻0, 已入驻1 入驻申请中2 */
    @Excel(name = "点位状态: 未入驻0, 已入驻1 入驻申请中2")
    @Schema(title = "点位状态: 未入驻0, 已入驻1 入驻申请中2")
    private Integer pointStatus;

    /** 点位编号 */
    @Excel(name = "点位编号")
    @Schema(title = "点位编号")
    private Long sort;

    /** 房间ID */
    @Excel(name = "房间ID")
    @Schema(title = "房间ID")
    private Long roomId;

}
