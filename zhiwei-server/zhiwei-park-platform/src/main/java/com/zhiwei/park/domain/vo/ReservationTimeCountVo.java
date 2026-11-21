package com.zhiwei.park.domain.vo;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 时段剩余预约次数VO
 *
 * @author ypf
 * @date 2026-10-30
 */
@Data
@Schema(description = "时段剩余预约次数")
public class ReservationTimeCountVo
{
    /** 预约时间段 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "预约时间段")
    private LocalDateTime time;

    /** 剩余可预约次数 */
    @Schema(title = "剩余可预约次数")
    private Integer count;
}
