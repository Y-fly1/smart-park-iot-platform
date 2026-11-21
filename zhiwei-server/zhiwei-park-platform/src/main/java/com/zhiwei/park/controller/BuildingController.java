package com.zhiwei.park.controller;

import com.zhiwei.common.annotation.Log;
import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.common.core.domain.R;
import com.zhiwei.common.enums.BusinessType;
import com.zhiwei.park.domain.Building;
import com.zhiwei.park.service.IBuildingService;
import com.zhiwei.park.domain.vo.CascadeVo;
import com.zhiwei.park.domain.vo.BuildingVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 楼栋Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/tenant/building")
@Tag(name = "楼栋相关接口")
public class BuildingController extends BaseController
{
    @Autowired
    private IBuildingService floorService;

    /**
     * 查询楼栋列表
     */
    @PreAuthorize("@ss.hasPermi('tenant:building:list')")
    @GetMapping("/list")
    @Operation(summary = "查询所有楼栋列表")
    public R<List<Building>> list()
    {
        List<Building> list = floorService.list();
        return R.ok(list);
    }

    /**
     * 获取楼栋详细信息
     */
    @PreAuthorize("@ss.hasPermi('tenant:building:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取楼栋详细信息")
    public R<Building> getInfo(@Schema(name = "楼栋ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("id") Long id)
    {
        return R.ok(floorService.selectBuildingById(id));
    }

    /**
     * 新增楼栋
     */
    @PreAuthorize("@ss.hasPermi('tenant:building:add')")
    @Log(title = "楼栋", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增楼栋")
    public AjaxResult add(@RequestBody Building building)
    {
        return toAjax(floorService.insertBuilding(building));
    }

    /**
     * 修改楼栋
     */
    @Operation(summary = "修改楼栋")
    @PreAuthorize("@ss.hasPermi('tenant:building:edit')")
    @Log(title = "楼栋", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Building building)
    {
        return toAjax(floorService.updateBuilding(building));
    }

    /**
     * 删除楼栋
     */
    @Operation(summary = "删除楼栋")
    @PreAuthorize("@ss.hasPermi('tenant:building:remove')")
    @Log(title = "楼栋", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@Schema(name = "楼栋ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(floorService.deleteBuildingByIds(ids));
    }

    @GetMapping("/getAllBuildingsWithNur")
    @Operation(summary = "获取所有楼栋 (负责企业)", description = "无需参数，获取所有楼栋，返回楼栋信息列表")
    public R<List<Building>> getAllBuildingsWithNur() {
        List<Building> list = floorService.selectAllByNur();
        return R.ok(list);
    }

    @GetMapping("/getRoomAndPointByPointStatus/{status}")
    @Operation(summary = "根据点位状态查询获取所有楼栋数据", description = "status：0-未入驻，1-已入驻")
    public R<List<CascadeVo>> getRoomAndPointByPointStatus(@Schema(name = "点位状态", requiredMode = Schema.RequiredMode.REQUIRED)
                                                       @PathVariable("status") Integer status) {
        return R.ok(floorService.getRoomAndPointByPointStatus(status));
    }

    @GetMapping("/getAllBuildingsWithDevice")
    public AjaxResult getAllBuildingsWithDevice(){
        return success(floorService.getAllBuildingsWithDevice());
    }
}
