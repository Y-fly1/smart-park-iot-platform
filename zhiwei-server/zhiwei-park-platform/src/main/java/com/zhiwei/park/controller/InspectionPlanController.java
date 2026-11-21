package com.zhiwei.park.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import com.zhiwei.park.domain.dto.InspectionPlanDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhiwei.common.annotation.Log;
import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.common.enums.BusinessType;
import com.zhiwei.park.domain.InspectionPlan;
import com.zhiwei.park.service.IInspectionPlanService;
import com.zhiwei.common.utils.poi.ExcelUtil;
import com.zhiwei.common.core.page.TableDataInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 巡检计划Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/park/inspectionPlan")
@Tag(name = "巡检计划相关接口")
public class InspectionPlanController extends BaseController
{
    @Autowired
    private IInspectionPlanService inspectionPlanService;


    @GetMapping("/all")
    @Operation(summary = "查询所有巡检计划")
    public AjaxResult listAll(){
        return success(inspectionPlanService.listAll());
    }

    /**
     * 查询巡检计划列表
     */
    @PreAuthorize("@ss.hasPermi('park:inspectionPlan:list')")
    @GetMapping("/list")
    @Operation(summary = "查询巡检计划列表")
    public TableDataInfo list(InspectionPlan inspectionPlan)
    {
        startPage();
        List<InspectionPlan> list = inspectionPlanService.selectInspectionPlanList(inspectionPlan);
        return getDataTable(list);
    }

    /**
     * 导出巡检计划列表
     */
    @PreAuthorize("@ss.hasPermi('park:inspectionPlan:export')")
    @Log(title = "巡检计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出巡检计划列表")
    public void export(HttpServletResponse response, InspectionPlan inspectionPlan)
    {
        List<InspectionPlan> list = inspectionPlanService.selectInspectionPlanList(inspectionPlan);
        ExcelUtil<InspectionPlan> util = new ExcelUtil<InspectionPlan>(InspectionPlan.class);
        util.exportExcel(response, list, "巡检计划数据");
    }

    /**
     * 获取巡检计划详细信息
     */
    @PreAuthorize("@ss.hasPermi('park:inspectionPlan:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取巡检计划详细信息")
    public AjaxResult getInfo(@Schema(name = "巡检计划ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("id") Long id)
    {
        return success(inspectionPlanService.selectInspectionPlanById(id));
    }

    /**
     * 新增巡检计划
     */
    @PreAuthorize("@ss.hasPermi('park:inspectionPlan:add')")
    @Log(title = "巡检计划", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增巡检计划")
    public AjaxResult add(@RequestBody InspectionPlanDto dto)
    {
        return toAjax(inspectionPlanService.insertInspectionPlan(dto));
    }

    /**
     * 修改巡检计划
     */
    @PreAuthorize("@ss.hasPermi('park:inspectionPlan:edit')")
    @Log(title = "巡检计划", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改巡检计划")
    public AjaxResult edit(@RequestBody InspectionPlanDto dto)
    {
        return toAjax(inspectionPlanService.updateInspectionPlan(dto));
    }

    /**
     * 删除巡检计划
     */
    @PreAuthorize("@ss.hasPermi('park:inspectionPlan:remove')")
    @Log(title = "巡检计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    @Operation(summary = "删除巡检计划")
    public AjaxResult remove(@Schema(name = "巡检计划ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long id)
    {
        return toAjax(inspectionPlanService.deleteInspectionPlanById(id));
    }
}
