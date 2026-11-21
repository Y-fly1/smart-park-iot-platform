package com.zhiwei.park.domain;

import com.zhiwei.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.zhiwei.common.core.domain.BaseEntity;

/**
 * 能效评估数据采集对象 energy_assessment_data
 * 
 * @author ypf
 * @date 2026-10-28
 */
@Data
@Schema(description = "能效评估数据采集对象")
public class EnergyAssessmentData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(title = "主键")
    private Long id;

    /** 基本信息 */
    @Schema(title = "基本信息")
    @Excel(name = "基本信息")
    private String basicInfo;

    /** 能效评估 */
    @Schema(title = "能效评估")
    @Excel(name = "能效评估")
    private String energyAssessment;

    /** 用电 */
    @Schema(title = "用电")
    @Excel(name = "用电")
    private String electricity;

    /** 用水 */
    @Schema(title = "用水")
    @Excel(name = "用水")
    private String water;

    /** 感知与沟通 */
    @Schema(title = "感知与沟通")
    @Excel(name = "感知与沟通")
    private String gas;

    /** 暖通 */
    @Schema(title = "暖通")
    @Excel(name = "暖通")
    private String hvac;

    /** 详细评估报告内容 */
    @Schema(title = "详细评估报告内容")
    @Excel(name = "详细评估报告内容")
    private String assessmentDetails;

}
