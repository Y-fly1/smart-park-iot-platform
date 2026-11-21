package com.zhiwei.park.domain.dto.energy;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 汇总数据
 */
@Data
@Schema(description = "企业能效评估数据")
public class TenantAssessmentDto {

    /**
     * 主键
     */
    @JsonProperty("id")
    @Schema(title = "主键")
    private Long id;

    /**
     * 基本信息
     */
    @JsonProperty("basicInfo")
    @Schema(title = "基本信息")
    private BasicInfo basicInfo;

    /**
     * 能效评估
     */
    @JsonProperty("energyAssessmentDto")
    @Schema(title = "能效评估")
    private EnergyAssessmentDto energyAssessmentDto;

    /**
     * 日常活动
     */
    @JsonProperty("dailyLivingActivities")
    @Schema(title = "日常活动")
    private ElectricityUsage electricityUsage;

    /**
     * 用水
     */
    @JsonProperty("mentalState")
    @Schema(title = "用水")
    private WaterUsage waterUsage;

    /**
     * 感知觉及沟通
     */
    @JsonProperty("perceptionAndCommunication")
    @Schema(title = "感知觉及沟通")
    private GasUsage gasUsage;

    /**
     * 暖通
     */
    @JsonProperty("socialParticipation")
    @Schema(title = "暖通")
    private HvacUsage hvacUsage;
}