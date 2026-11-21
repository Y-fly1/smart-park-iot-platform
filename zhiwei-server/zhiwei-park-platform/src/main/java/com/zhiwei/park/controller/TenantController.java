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
import com.zhiwei.park.domain.Tenant;
import com.zhiwei.park.service.ITenantService;
import com.zhiwei.common.utils.poi.ExcelUtil;
import com.zhiwei.common.core.page.TableDataInfo;

/**
 * 企业表Controller
 * 
 * @author ypf
 * @date 2026-10-30
 */
@RestController
@RequestMapping("/park/tenant")
@Tag(name = "企业表相关接口")
public class TenantController extends BaseController
{
    @Autowired
    private ITenantService elderService;

    /**
     * 查询企业表列表
     */
    @PreAuthorize("@ss.hasPermi('park:tenant:list')")
    @GetMapping("/list")
    @Operation(summary = "查询企业表列表")
    public TableDataInfo list(Tenant tenant)
    {
        startPage();
        List<Tenant> list = elderService.selectTenantList(tenant);
        return getDataTable(list);
    }

    /**
     * 导出企业表列表
     */
    @PreAuthorize("@ss.hasPermi('park:tenant:export')")
    @Log(title = "企业表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出企业表列表")
    public void export(HttpServletResponse response, Tenant tenant)
    {
        List<Tenant> list = elderService.selectTenantList(tenant);
        ExcelUtil<Tenant> util = new ExcelUtil<Tenant>(Tenant.class);
        util.exportExcel(response, list, "企业表数据");
    }

    /**
     * 获取企业表详细信息
     */
    @PreAuthorize("@ss.hasPermi('park:tenant:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取企业表详细信息")
    public AjaxResult getInfo(@Schema(name = "企业表ID", requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("id") Long id)
    {
        return success(elderService.selectTenantById(id));
    }

    /**
     * 新增企业表
     */
    @PreAuthorize("@ss.hasPermi('park:tenant:add')")
    @Log(title = "企业表", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增企业表")
    public AjaxResult add(@RequestBody Tenant tenant)
    {
        return toAjax(elderService.insertTenant(tenant));
    }

    /**
     * 修改企业表
     */
    @PreAuthorize("@ss.hasPermi('park:tenant:edit')")
    @Log(title = "企业表", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改企业表")
    public AjaxResult edit(@RequestBody Tenant tenant)
    {
        return toAjax(elderService.updateTenant(tenant));
    }

    /**
     * 删除企业表
     */
    @PreAuthorize("@ss.hasPermi('park:tenant:remove')")
    @Log(title = "企业表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Operation(summary = "删除企业表")
    public AjaxResult remove(@Schema(name = "企业表ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(elderService.deleteTenantByIds(ids));
    }
}
