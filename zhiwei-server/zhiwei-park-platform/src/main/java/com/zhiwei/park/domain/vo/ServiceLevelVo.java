package com.zhiwei.park.domain.vo;

import com.zhiwei.park.domain.ServiceLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "服务等级VO")
public class ServiceLevelVo extends ServiceLevel {

    /**
     * 巡检计划名称
     */
    @Schema(title = "巡检计划名称")
    private String planName;
}
