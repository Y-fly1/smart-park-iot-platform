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
import com.zhiwei.park.domain.SettleInConfig;
import com.zhiwei.park.service.ISettleInConfigService;
import com.zhiwei.common.utils.poi.ExcelUtil;
import com.zhiwei.common.core.page.TableDataInfo;

/**
 * 配置表Controller
 * 
 * @author ypf
 * @date 2026-10-30
 */
@RestController
@RequestMapping("/park/settleInConfig")
@Tag(name = "配置表相关接口")
public class SettleInConfigController extends BaseController
{
    @Autowired
    private ISettleInConfigService settleInConfigService;

    /**
     * 查询配置表列表
     */
    @PreAuthorize("@ss.hasPermi('park:settleConfig:list')")
    @GetMapping("/list")
    @Operation(summary = "查询配置表列表")
    public TableDataInfo list(SettleInConfig settleInConfig)
    {
        startPage();
        List<SettleInConfig> list = settleInConfigService.selectSettleInConfigList(settleInConfig);
        return getDataTable(list);
    }

    /**
     * 导出配置表列表
     */
    @PreAuthorize("@ss.hasPermi('park:settleConfig:export')")
    @Log(title = "配置表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出配置表列表")
    public void export(HttpServletResponse response, SettleInConfig settleInConfig)
    {
        List<SettleInConfig> list = settleInConfigService.selectSettleInConfigList(settleInConfig);
        ExcelUtil<SettleInConfig> util = new ExcelUtil<SettleInConfig>(SettleInConfig.class);
        util.exportExcel(response, list, "配置表数据");
    }

    /**
     * 获取配置表详细信息
     */
    @PreAuthorize("@ss.hasPermi('park:settleConfig:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取配置表详细信息")
    public AjaxResult getInfo(@Schema(name = "配置表ID", requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("id") Long id)
    {
        return success(settleInConfigService.selectSettleInConfigById(id));
    }

    /**
     * 新增配置表
     */
    @PreAuthorize("@ss.hasPermi('park:settleConfig:add')")
    @Log(title = "配置表", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增配置表")
    public AjaxResult add(@RequestBody SettleInConfig settleInConfig)
    {
        return toAjax(settleInConfigService.insertSettleInConfig(settleInConfig));
    }

    /**
     * 修改配置表
     */
    @PreAuthorize("@ss.hasPermi('park:settleConfig:edit')")
    @Log(title = "配置表", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改配置表")
    public AjaxResult edit(@RequestBody SettleInConfig settleInConfig)
    {
        return toAjax(settleInConfigService.updateSettleInConfig(settleInConfig));
    }

    /**
     * 删除配置表
     */
    @PreAuthorize("@ss.hasPermi('park:settleConfig:remove')")
    @Log(title = "配置表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Operation(summary = "删除配置表")
    public AjaxResult remove(@Schema(name = "配置表ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(settleInConfigService.deleteSettleInConfigByIds(ids));
    }
}
