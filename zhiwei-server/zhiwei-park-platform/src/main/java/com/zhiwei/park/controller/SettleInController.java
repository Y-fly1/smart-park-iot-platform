package com.zhiwei.park.controller;

import java.util.List;
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
import com.zhiwei.park.domain.SettleIn;
import com.zhiwei.park.domain.dto.SettleInApplyDto;
import com.zhiwei.park.service.ISettleInService;
import com.zhiwei.common.utils.poi.ExcelUtil;
import com.zhiwei.common.core.page.TableDataInfo;

/**
 * 入驻表Controller
 * 
 * @author ypf
 * @date 2026-10-30
 */
@RestController
@RequestMapping("/park/settleIn")
@Tag(name = "入驻表相关接口")
public class SettleInController extends BaseController
{
    @Autowired
    private ISettleInService settleInService;

    /**
     * 查询入驻表列表
     */
    @PreAuthorize("@ss.hasPermi('park:settleIn:list')")
    @GetMapping("/list")
    @Operation(summary = "查询入驻表列表")
    public TableDataInfo list(SettleIn settleIn)
    {
        startPage();
        List<SettleIn> list = settleInService.selectSettleInList(settleIn);
        return getDataTable(list);
    }

    /**
     * 导出入驻表列表
     */
    @PreAuthorize("@ss.hasPermi('park:settleIn:export')")
    @Log(title = "入驻表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出入驻表列表")
    public void export(HttpServletResponse response, SettleIn settleIn)
    {
        List<SettleIn> list = settleInService.selectSettleInList(settleIn);
        ExcelUtil<SettleIn> util = new ExcelUtil<SettleIn>(SettleIn.class);
        util.exportExcel(response, list, "入驻表数据");
    }

    /**
     * 获取入驻表详细信息
     */
    @PreAuthorize("@ss.hasPermi('park:settleIn:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取入驻表详细信息")
    public AjaxResult getInfo(@Schema(name = "入驻表ID", requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("id") Long id)
    {
        return success(settleInService.selectSettleInById(id));
    }

    /**
     * 新增入驻表
     */
    @PreAuthorize("@ss.hasPermi('park:settleIn:add')")
    @Log(title = "入驻表", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增入驻表")
    public AjaxResult add(@RequestBody SettleIn settleIn)
    {
        return toAjax(settleInService.insertSettleIn(settleIn));
    }

    /**
     * 修改入驻表
     */
    @PreAuthorize("@ss.hasPermi('park:settleIn:edit')")
    @Log(title = "入驻表", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改入驻表")
    public AjaxResult edit(@RequestBody SettleIn settleIn)
    {
        return toAjax(settleInService.updateSettleIn(settleIn));
    }

    /**
     * 申请入驻
     */
    @Log(title = "入驻办理", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    @Operation(summary = "申请入驻")
    public AjaxResult apply(@RequestBody SettleInApplyDto dto)
    {
        settleInService.applySettleIn(dto);
        return success();
    }

    /**
     * 查询入驻详情
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "查询入驻详情")
    public AjaxResult detail(@Schema(name = "入驻ID", requiredMode = Schema.RequiredMode.REQUIRED)
                             @PathVariable("id") Long id)
    {
        return success(settleInService.getSettleInDetail(id));
    }

    /**
     * 删除入驻表
     */
    @PreAuthorize("@ss.hasPermi('park:settleIn:remove')")
    @Log(title = "入驻表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Operation(summary = "删除入驻表")
    public AjaxResult remove(@Schema(name = "入驻表ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(settleInService.deleteSettleInByIds(ids));
    }
}
