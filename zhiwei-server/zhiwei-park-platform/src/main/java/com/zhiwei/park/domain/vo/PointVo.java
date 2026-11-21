package com.zhiwei.park.domain.vo;

import com.zhiwei.common.core.domain.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "点位VO")
public class PointVo {


    @Schema(title = "点位ID")
    private Long id;

    /**
     * 点位编号
     */
    @Schema(title = "点位编号")
    private String pointCode;

    /**
     * 点位状态: 未入驻0, 已入驻1 入驻申请中2
     */
    @Schema(title = "点位状态: 未入驻0, 已入驻1 入驻申请中2")
    private Integer pointStatus;

    /**
     * 房间ID
     */
    @Schema(title = "房间ID")
    private Long roomId;

    /**
     * 企业名称
     */
    @Schema(title = "企业名称")
    private String tenantName;

    /**
     * 企业id
     */
    @Schema(title = "企业id")
    private Long tenantId;


    @Schema(title = "巡检员")
    private List<SysUser> userVos;

    @Schema(title = "点位设备列表")
    private List<DeviceVo> deviceVos;

}
