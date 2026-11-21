package com.zhiwei.park.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "巡检计划DTO")
public class InspectionPlanDto {


    @Schema(title = "编号")
    private Long id;

    /**
     * 排序号
     */
    @Schema(title = "排序号")
    private Integer sortNo;

    /**
     * 计划名称
     */
    @Schema(title = "计划名称")
    private String planName;

    /**
     * 状态（0：禁用，1：启用）
     */
    @Schema(title = "状态（0：禁用，1：启用）")
    private Integer status;

    /**
     * 巡检计划关联项目列表
     */
    @Schema(title = "巡检计划关联项目列表")
    List<InspectionItemPlanDto> projectPlans;
}