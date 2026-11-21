package com.zhiwei.park.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 能耗账单中的单条异常数据
 *
 * @author mr.wu
 */
@Data
@Schema(description = "异常数据项")
public class AbnormalDataVO {

    /** 异常结论 */
    @Schema(title = "异常结论")
    private String conclusion;

    /** 用能检测项目 */
    @Schema(title = "用能检测项目")
    private String meteringItem;

    /** 检查结果 */
    @Schema(title = "检查结果")
    private String result;

    /** 参考值 */
    @Schema(title = "参考值")
    private String referenceValue;

    /** 单位 */
    @Schema(title = "单位")
    private String unit;

    /** 异常解读 */
    @Schema(title = "异常解读")
    private String interpret;

    /** 建议 */
    @Schema(title = "建议")
    private String advice;
}