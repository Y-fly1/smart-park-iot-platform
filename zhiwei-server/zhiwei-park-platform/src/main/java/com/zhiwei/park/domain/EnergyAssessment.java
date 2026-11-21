package com.zhiwei.park.domain;

import com.zhiwei.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.zhiwei.common.core.domain.BaseEntity;

/**
 * 能效评估记录对象 energy_assessment
 * 
 * @author ypf
 * @date 2026-10-28
 */
@Data
@Schema(description = "能效评估记录对象")
public class EnergyAssessment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(title = "主键")
    private Long id;

    /** 企业名称 */
    @Schema(title = "企业名称")
    @Excel(name = "企业名称")
    private String tenantName;

    /** 统一社会信用代码码 */
    @Schema(title = "统一社会信用代码码")
    @Excel(name = "统一社会信用代码码")
    private String creditCode;

    /** 企业id */
    @Schema(title = "企业id")
    @Excel(name = "企业id")
    private Long tenantId;

    /** 核心建议，0-不建议入驻，1-建议入驻 */
    @Schema(title = "核心建议，0-不建议入驻，1-建议入驻")
    @Excel(name = "核心建议，0-不建议入驻，1-建议入驻")
    private Integer coreSuggestion;

    /** 入驻状态：0-未入驻，1-已入驻 */
    @Schema(title = "入驻状态：0-未入驻，1-已入驻")
    @Excel(name = "入驻状态：0-未入驻，1-已入驻")
    private Integer settleInStatus;

    /** 评估进度：0-评估中，1-已完成，2-已取消 */
    @Schema(title = "评估进度：0-评估中，1-已完成，2-已取消")
    @Excel(name = "评估进度：0-评估中，1-已完成，2-已取消")
    private Integer evaluationProgress;

}
