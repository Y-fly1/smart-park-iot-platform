package com.zhiwei.park.controller;

import com.zhiwei.common.annotation.Log;
import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.common.core.domain.R;
import com.zhiwei.common.core.page.TableDataInfo;
import com.zhiwei.common.enums.BusinessType;
import com.zhiwei.park.domain.Point;
import com.zhiwei.park.service.IPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 点位Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/tenant/point")
@Tag(name = "点位相关接口")
public class PointController extends BaseController
{
    @Autowired
    private IPointService bedService;

    /**
     * 查询点位列表
     */
    @PreAuthorize("@ss.hasPermi('tenant:point:list')")
    @GetMapping("/list")
    @Operation(summary = "查询点位列表")
    public TableDataInfo list(Point point)
    {
        startPage();
        List<Point> list = bedService.selectPointList(point);
        return getDataTable(list);
    }

    /**
     * 获取点位详细信息
     */
    @PreAuthorize("@ss.hasPermi('tenant:point:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取点位详细信息")
    public R<Point> getInfo(@Schema(name = "点位ID", requiredMode = Schema.RequiredMode.REQUIRED)  @PathVariable("id") Long id)
    {
        return R.ok(bedService.selectPointById(id));
    }

    /**
     * 新增点位
     */
    @PreAuthorize("@ss.hasPermi('tenant:point:add')")
    @Log(title = "点位", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增点位")
    public AjaxResult add(@RequestBody Point point)
    {
        return toAjax(bedService.insertPoint(point));
    }

    /**
     * 修改点位
     */
    @Operation(summary = "修改点位")
    @PreAuthorize("@ss.hasPermi('tenant:point:edit')")
    @Log(title = "点位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Point point)
    {
        return toAjax(bedService.updatePoint(point));
    }

    /**
     * 删除点位
     */
    @Operation(summary = "删除点位")
    @PreAuthorize("@ss.hasPermi('tenant:point:remove')")
    @Log(title = "点位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@Schema(name = "点位ID", requiredMode = Schema.RequiredMode.REQUIRED)  @PathVariable Long[] ids)
    {
        return toAjax(bedService.deletePointByIds(ids));
    }
}
