package com.zhiwei.park.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "巡检计划VO")
public class InspectionPlanVo {

    /**
     * 巡检计划id
     */
    @Schema(title = "巡检计划id")
    private Long id;

    /**
     * 排序号
     */
    @Schema(title = "巡检计划排序号")
    private Integer sortNo;

    @Schema(title = "巡检计划名称")
    private String planName;


    @Schema(title = "状态（0：禁用，1：启用）")
    private Integer status;

    @Schema(title = "巡检计划项目计划列表")
    List<InspectionItemPlanVo> projectPlans;

}