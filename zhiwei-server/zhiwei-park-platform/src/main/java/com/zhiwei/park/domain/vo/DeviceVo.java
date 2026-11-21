package com.zhiwei.park.domain.vo;

import com.zhiwei.park.domain.DeviceData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 设备VO
 *
 * @author ypf
 * @date 2026-11-03
 */
@Data
@Schema(description = "设备VO")
public class DeviceVo {

    /** 设备主键 */
    @Schema(title = "设备主键")
    private Long id;

    /** 物联网设备ID */
    @Schema(title = "物联网设备ID")
    private String iotId;

    /** 设备名称 */
    @Schema(title = "设备名称")
    private String deviceName;

    /** 产品key */
    @Schema(title = "产品key")
    private String productKey;

    /** 产品名称 */
    @Schema(title = "产品名称")
    private String productName;

    /** 设备最新上报数据 */
    @Schema(title = "设备最新上报数据")
    private List<DeviceData> deviceDataVos;
}
