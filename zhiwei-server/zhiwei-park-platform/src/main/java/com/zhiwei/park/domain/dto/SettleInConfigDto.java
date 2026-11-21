package com.zhiwei.park.domain.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwei.park.domain.SettleInConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入驻配置DTO（含点位、房间等申请入驻所需信息）
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "入驻配置信息")
public class SettleInConfigDto extends SettleInConfig
{
    private static final long serialVersionUID = 1L;

    /** 点位ID */
    @Schema(title = "点位ID")
    private Long pointId;

    /** 房间ID */
    @Schema(title = "房间ID")
    private Long roomId;

    /** 楼栋ID */
    @Schema(title = "楼栋ID")
    private Long buildingId;

    /** 楼栋名称 */
    @Schema(title = "楼栋名称")
    private String floorName;

    /** 房间编号 */
    @Schema(title = "房间编号")
    private String code;

    /** 入驻开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "入驻开始时间")
    private LocalDateTime startDate;

    /** 入驻结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "入驻结束时间")
    private LocalDateTime endDate;
}
