package com.zhiwei.park.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备上报数据VO
 *
 * @author ypf
 * @date 2026-11-02
 */
@Data
@Schema(description = "设备上报数据VO")
public class DeviceReportedDataVo {

    /** 功能标识 */
    @Schema(title = "功能标识")
    private String functionId;

    /** 上报时间（北京时间） */
    @Schema(title = "上报时间（北京时间）")
    private LocalDateTime eventTime;

    /** 上报值 */
    @Schema(title = "上报值")
    private Object value;
}
