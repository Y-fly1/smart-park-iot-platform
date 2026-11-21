package com.zhiwei.park.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "房间VO")
public class RoomVo {

    @Schema(title = "房间ID")
    private Long id;

    @Schema(title = "楼栋名称")
    private String buildingName;

    @Schema(title = "楼栋ID")
    private String buildingId;

    @Schema(title = "房间ID")
    private String roomId;

    @Schema(title = "房间编号")
    private String code;

    @Schema(title = "房间价格")
    private String price;

    @Schema(title = "点位列表")
    private List<PointVo> pointVoList;

    @Schema(title = "房间设备列表")
    private List<DeviceVo> deviceVos;

}