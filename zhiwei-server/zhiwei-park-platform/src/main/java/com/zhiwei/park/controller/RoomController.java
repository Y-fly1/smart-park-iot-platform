package com.zhiwei.park.controller;

import com.zhiwei.common.annotation.Log;
import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.common.core.domain.R;
import com.zhiwei.common.core.page.TableDataInfo;
import com.zhiwei.common.enums.BusinessType;
import com.zhiwei.park.domain.Room;
import com.zhiwei.park.service.IRoomService;
import com.zhiwei.park.domain.vo.RoomVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 房间Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/tenant/room")
@Tag(name = "房间相关接口")
public class RoomController extends BaseController
{
    @Autowired
    private IRoomService roomService;


    @GetMapping("/getRoomsWithNurByBuildingId/{buildingId}")
    @Operation(summary = "获取所有房间（负责企业）")
    public R<List<RoomVo>> getRoomsWithNurByBuildingId(@PathVariable("buildingId") Long buildingId) {
        List<RoomVo> list = roomService.getRoomsWithNurByBuildingId(buildingId);
        return R.ok(list);
    }

    @GetMapping("/getRoomsWithDeviceByBuildingId/{buildingId}")
    @Operation(summary = "获取房间中的智能设备及数据")
    public R<List<RoomVo>> getRoomsWithDeviceByBuildingId(@Schema(name = "楼栋ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("buildingId") Long buildingId) {
        List<RoomVo> list = roomService.getRoomsWithDeviceByBuildingId(buildingId);
        return R.ok(list);
    }

    @GetMapping("/getRoomsByBuildingId/{buildingId}")
    @Operation(summary = "获取所有房间（入驻配置）")
    public R<List<RoomVo>> getRoomsByBuildingId(@Schema(name = "楼栋ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("buildingId") Long buildingId) {
        List<RoomVo> list = roomService.getRoomsByBuildingId(buildingId);
        return R.ok(list);
    }
    @GetMapping("/one/{id}")
    @Operation(summary = "查询房间数据(楼栋、房间、价格)")
    public R<RoomVo> getOne(@Schema(name = "房间ID", requiredMode = Schema.RequiredMode.REQUIRED)
                            @PathVariable("id") Long id) {
        return R.ok(roomService.getRoomDetailById(id));
    }

    /**
     * 查询房间列表
     */
    @PreAuthorize("@ss.hasPermi('tenant:room:list')")
    @GetMapping("/list")
    @Operation(summary = "查询房间列表")
    public TableDataInfo list(Room room)
    {
        startPage();
        List<Room> list = roomService.selectRoomList(room);
        return getDataTable(list);
    }

    /**
     * 获取房间详细信息
     */
    @PreAuthorize("@ss.hasPermi('tenant:room:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取房间详细信息")
    public R<Room> getInfo(@Schema(name = "房间ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("id") Long id)
    {
        return R.ok(roomService.selectRoomById(id));
    }

    /**
     * 新增房间
     */
    @PreAuthorize("@ss.hasPermi('tenant:room:add')")
    @Log(title = "房间", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增房间")
    public AjaxResult add(@RequestBody Room room)
    {
        return toAjax(roomService.insertRoom(room));
    }

    /**
     * 修改房间
     */
    @Operation(summary = "修改房间")
    @PreAuthorize("@ss.hasPermi('tenant:room:edit')")
    @Log(title = "房间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Room room)
    {
        return toAjax(roomService.updateRoom(room));
    }

    /**
     * 删除房间
     */
    @Operation(summary = "删除房间")
    @PreAuthorize("@ss.hasPermi('tenant:room:remove')")
    @Log(title = "房间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@Schema(name = "房间ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("ids") Long[] ids)
    {
        return toAjax(roomService.deleteRoomByIds(ids));
    }

}
