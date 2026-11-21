package com.zhiwei.park.domain.vo;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwei.park.domain.SettleInConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入驻配置VO（含点位编号）
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "入驻配置详情")
public class SettleInConfigVo extends SettleInConfig
{
    private static final long serialVersionUID = 1L;

    /** 点位编号 */
    @Schema(title = "点位编号")
    private String pointCode;

    /** 入驻开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "入驻开始时间")
    private LocalDateTime startDate;

    /** 入驻结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "入驻结束时间")
    private LocalDateTime endDate;
}
