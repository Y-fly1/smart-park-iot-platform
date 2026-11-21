package com.zhiwei.park.controller;

import java.util.List;

import com.zhiwei.park.domain.dto.energy.TenantAssessmentDto;
import jakarta.servlet.http.HttpServletResponse;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.zhiwei.park.domain.EnergyAssessment;
import com.zhiwei.park.service.IEnergyAssessmentService;
import com.zhiwei.common.utils.poi.ExcelUtil;
import com.zhiwei.common.core.page.TableDataInfo;

/**
 * 能效评估记录Controller
 * 
 * @author ypf
 * @date 2026-10-28
 */
@RestController
@RequestMapping("/park/energyAssessment")
@Tag(name = "能效评估记录相关接口")
public class EnergyAssessmentController extends BaseController
{
    @Autowired
    private IEnergyAssessmentService energyAssessmentService;

    /**
     * 查询能效评估记录列表
     */
    @PreAuthorize("@ss.hasPermi('park:energyAssessment:list')")
    @GetMapping("/list")
    @Operation(summary = "查询能效评估记录列表")
    public TableDataInfo list(EnergyAssessment energyAssessment)
    {
        startPage();
        List<EnergyAssessment> list = energyAssessmentService.selectEnergyAssessmentList(energyAssessment);
        return getDataTable(list);
    }

    /**
     * 导出能效评估记录列表
     */
    @PreAuthorize("@ss.hasPermi('park:energyAssessment:export')")
    @Log(title = "能效评估记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出能效评估记录列表")
    public void export(HttpServletResponse response, EnergyAssessment energyAssessment)
    {
        List<EnergyAssessment> list = energyAssessmentService.selectEnergyAssessmentList(energyAssessment);
        ExcelUtil<EnergyAssessment> util = new ExcelUtil<EnergyAssessment>(EnergyAssessment.class);
        util.exportExcel(response, list, "能效评估记录数据");
    }

    /**
     * 获取能效评估记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('park:energyAssessment:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取能效评估记录详细信息")
    public AjaxResult getInfo(@Schema(name = "能效评估记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("id") Long id)
    {
        return success(energyAssessmentService.selectEnergyAssessmentById(id));
    }

    /**
     * 新增能效评估记录
     */
    @PreAuthorize("@ss.hasPermi('park:energyAssessment:add')")
    @Log(title = "能效评估记录", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增能效评估记录")
    public AjaxResult add(@RequestBody TenantAssessmentDto dto)
    {
        return success(energyAssessmentService.insertOrUpdateEnergyAssessment(dto));
    }

    /**
     * 修改能效评估记录
     */
    @PreAuthorize("@ss.hasPermi('park:energyAssessment:edit')")
    @Log(title = "能效评估记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改能效评估记录")
    public AjaxResult edit(@RequestBody TenantAssessmentDto dto)
    {
        return success(energyAssessmentService.insertOrUpdateEnergyAssessment(dto));
    }

    /**
     * AI评估
     */
    @PostMapping("/assessmentData")
    public AjaxResult assessmentData(@RequestBody TenantAssessmentDto dto){
        return success(energyAssessmentService.assessmentData(dto));
    }

    /**
     * 查看评估详情(评估结果)
     */
    @GetMapping("/report/{id}")
    public AjaxResult getAssessmentDetail(@PathVariable Long id) {
        return success(energyAssessmentService.getAssessmentDetail(id));
    }

    /**
     * 查看企业信息详情(入驻字段填充)
     */
    @GetMapping("/tenant/{id}")
    @Operation(summary = "查看企业信息详情(入驻字段填充)")
    public AjaxResult getTenantBasicInfo(@PathVariable("id") Long id) {
        return success(energyAssessmentService.getTenantBasicInfo(id));
    }

    /**
     * 取消评估
     */
    @PutMapping(value = "{id}")
    @Operation(summary = "取消评估")
    public AjaxResult cancelAssessment(@PathVariable Long id) {
        energyAssessmentService.cancelAssessment(id);
        return success();
    }



    /**
     * 删除能效评估记录
     */
    @PreAuthorize("@ss.hasPermi('park:energyAssessment:remove')")
    @Log(title = "能效评估记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Operation(summary = "删除能效评估记录")
    public AjaxResult remove(@Schema(name = "能效评估记录ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(energyAssessmentService.deleteEnergyAssessmentByIds(ids));
    }


}
