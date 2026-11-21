package com.zhiwei.park.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwei.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.zhiwei.common.core.domain.BaseEntity;

/**
 * 能效评估报告对象 energy_assessment_report
 * 
 * @author ypf
 * @date 2026-10-28
 */
@Data
@Schema(description = "能效评估报告对象")
public class EnergyAssessmentReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(title = "主键ID")
    private Long id;

    /** 能效评估ID */
    @Schema(title = "能效评估ID")
    @Excel(name = "能效评估ID")
    private String energyAssessmentId;

    /** 评估时间 */
    @Schema(title = "评估时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "评估时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date assessmentTime;

    /** 评估员姓名 */
    @Schema(title = "评估员姓名")
    @Excel(name = "评估员姓名")
    private String assessorName;

    /** 推荐入驻区域类型（如：标准双人间） */
    @Schema(title = "推荐入驻区域类型（如：标准双人间）")
    @Excel(name = "推荐入驻区域类型", readConverterExp = "如=：标准双人间")
    private String recommendedAreaType;

    /** 推荐服务等级（如：一级服务等级） */
    @Schema(title = "推荐服务等级（如：一级服务等级）")
    @Excel(name = "推荐服务等级", readConverterExp = "如=：一级服务等级")
    private String recommendedServiceLevel;

    /** 巡检重点（多条用分号分隔，如：血压管控；肢体协助；认知干预） */
    @Schema(title = "巡检重点（多条用分号分隔，如：血压管控；肢体协助；认知干预）")
    @Excel(name = "巡检重点", readConverterExp = "多=条用分号分隔，如：血压管控；肢体协助；认知干预")
    private String energyFocus;

    /** 建议说明（如：企业基础能效状况稳定，日常生活能部分自理） */
    @Schema(title = "建议说明（如：企业基础能效状况稳定，日常生活能部分自理）")
    @Excel(name = "建议说明", readConverterExp = "如=：企业基础能效状况稳定，日常生活能部分自理")
    private String suggestionDescription;

    /** 用电等级 */
    @Schema(title = "用电等级")
    @Excel(name = "用电等级")
    private String electricityLevel;

    /** 用水等级 */
    @Schema(title = "用水等级")
    @Excel(name = "用水等级")
    private String waterLevel;

    /** 燃气等级 */
    @Schema(title = "燃气等级")
    @Excel(name = "燃气等级")
    private String gasLevel;

    /** 暖通等级 */
    @Schema(title = "暖通等级")
    @Excel(name = "暖通等级")
    private String hvacLevel;

    /** 入驻企业能效初步等级 */
    @Schema(title = "入驻企业能效初步等级")
    @Excel(name = "入驻企业能效初步等级")
    private String initialGrade;

    /** 入驻企业能力最终等级 */
    @Schema(title = "入驻企业能力最终等级")
    @Excel(name = "入驻企业能力最终等级")
    private String finalGrade;

    /** 等级变更依据说明 */
    @Schema(title = "等级变更依据说明")
    @Excel(name = "等级变更依据说明")
    private String gradeChangeReason;

    /** 联系人配合事项（多条用分号分隔） */
    @Schema(title = "联系人配合事项（多条用分号分隔）")
    @Excel(name = "联系人配合事项", readConverterExp = "多=条用分号分隔")
    private String tenantCooperation;

    /** 设备改造建议（如：低盐低脂饮食，控制主食摄入量，避免高糖食物） */
    @Schema(title = "设备改造建议（如：低盐低脂饮食，控制主食摄入量，避免高糖食物）")
    @Excel(name = "设备改造建议", readConverterExp = "如=：低盐低脂饮食，控制主食摄入量，避免高糖食物")
    private String equipmentSuggestion;

    /** 运行策略建议（如：多关注企业情绪变化，每日进行不少于10分钟的一对一沟通） */
    @Schema(title = "运行策略建议（如：多关注企业情绪变化，每日进行不少于10分钟的一对一沟通）")
    @Excel(name = "运行策略建议", readConverterExp = "如=：多关注企业情绪变化，每日进行不少于10分钟的一对一沟通")
    private String operationSuggestion;

    /** 园区准备事项（多条用分号分隔，如：提前准备轮椅；房间配置；运维人员适配） */
    @Schema(title = "园区准备事项（多条用分号分隔，如：提前准备轮椅；房间配置；运维人员适配）")
    @Excel(name = "园区准备事项", readConverterExp = "多=条用分号分隔，如：提前准备轮椅；房间配置；运维人员适配")
    private String parkPreparation;

    /** 能效评分 */
    @Schema(title = "能效评分")
    @Excel(name = "能效评分")
    private String energyScore;

    /** 严重危险(能效, 提示, 风险, 危险, 严重危险) */
    @Schema(title = "严重危险(能效, 提示, 风险, 危险, 严重危险)")
    @Excel(name = "严重危险(能效, 提示, 风险, 危险, 严重危险)")
    private String riskLevel;

    /** 报告总结 */
    @Schema(title = "报告总结")
    @Excel(name = "报告总结")
    private String reportSummary;

    /** 异常分析 */
    @Schema(title = "异常分析")
    @Excel(name = "异常分析")
    private String abnormalAnalysis;

    /** 能效系统分值 */
    @Schema(title = "能效系统分值")
    @Excel(name = "能效系统分值")
    private String systemScore;

    /** 核心建议，0-不建议入驻，1-建议入驻 */
    @Schema(title = "核心建议，0-不建议入驻，1-建议入驻")
    @Excel(name = "核心建议，0-不建议入驻，1-建议入驻")
    private Integer coreSuggestion;

    /** 入驻状态：0-未入驻，1-已入驻 */
    @Schema(title = "入驻状态：0-未入驻，1-已入驻")
    @Excel(name = "入驻状态：0-未入驻，1-已入驻")
    private Integer settleInStatus;

}
