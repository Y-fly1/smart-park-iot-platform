package com.zhiwei.park.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "楼栋VO")
public class BuildingVo {

    @Schema(title = "楼栋ID")
    private Long id;

    @Schema(title = "楼栋名称")
    private String name;

    @Schema(title = "楼栋编号")
    private Integer code;

    @Schema(title = "房间")
    private List<RoomVo> roomVoList;

}
