package com.zhiwei.park.controller;

import com.zhiwei.common.annotation.Log;
import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.common.core.page.TableDataInfo;
import com.zhiwei.common.enums.BusinessType;
import com.zhiwei.common.utils.poi.ExcelUtil;
import com.zhiwei.park.domain.AreaType;
import com.zhiwei.park.service.IAreaTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 区域类型Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/tenant/areaType")
@Tag(name = "区域类型相关接口")
public class AreaTypeController extends BaseController
{
    @Autowired
    private IAreaTypeService areaTypeService;

    /**
     * 查询区域类型列表
     */
    @PreAuthorize("@ss.hasPermi('tenant:areaType:list')")
    @GetMapping("/list")
    @Operation(summary = "查询区域类型列表")
    public TableDataInfo list(AreaType areaType)
    {
        startPage();
        List<AreaType> list = areaTypeService.selectAreaTypeList(areaType);
        return getDataTable(list);
    }

    /**
     * 查询区域类型列表
     */
    @PreAuthorize("@ss.hasPermi('tenant:areaType:list')")
    @GetMapping("/listAll")
    @Operation(summary = "查询所有区域类型列表")
    public AjaxResult list()
    {
        List<AreaType> list = areaTypeService.list();
        return success(list);
    }

    /**
     * 导出区域类型列表
     */
    @PreAuthorize("@ss.hasPermi('tenant:areaType:export')")
    @Log(title = "区域类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出区域类型列表")
    public void export(HttpServletResponse response, AreaType areaType)
    {
        List<AreaType> list = areaTypeService.selectAreaTypeList(areaType);
        ExcelUtil<AreaType> util = new ExcelUtil<AreaType>(AreaType.class);
        util.exportExcel(response, list, "区域类型数据");
    }

    /**
     * 获取区域类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('tenant:areaType:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取区域类型详细信息")
    public AjaxResult getInfo(@Schema(name = "区域类型ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("id") Long id)
    {
        return success(areaTypeService.selectAreaTypeById(id));
    }

    /**
     * 新增区域类型
     */
    @PreAuthorize("@ss.hasPermi('tenant:areaType:add')")
    @Log(title = "区域类型", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增区域类型")
    public AjaxResult add(@RequestBody AreaType areaType)
    {
        return toAjax(areaTypeService.insertAreaType(areaType));
    }

    /**
     * 修改区域类型
     */
    @Operation(summary = "修改区域类型")
    @PreAuthorize("@ss.hasPermi('tenant:areaType:edit')")
    @Log(title = "区域类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AreaType areaType)
    {
        return toAjax(areaTypeService.updateAreaType(areaType));
    }

    /**
     * 删除区域类型
     */
    @Operation(summary = "删除区域类型")
    @PreAuthorize("@ss.hasPermi('tenant:areaType:remove')")
    @Log(title = "区域类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@Schema(name = "区域类型ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(areaTypeService.deleteAreaTypeByIds(ids));
    }
}
