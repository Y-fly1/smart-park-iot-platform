package com.zhiwei.park.controller;

import java.util.List;

import com.zhiwei.park.domain.vo.ServiceLevelVo;
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
import com.zhiwei.park.domain.ServiceLevel;
import com.zhiwei.park.service.IServiceLevelService;
import com.zhiwei.common.core.page.TableDataInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 服务等级Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/park/serviceLevel")
@Tag(name = "服务等级相关接口")
public class ServiceLevelController extends BaseController
{
    @Autowired
    private IServiceLevelService serviceLevelService;

    /**
     * 查询服务等级列表
     */
    @PreAuthorize("@ss.hasPermi('park:serviceLevel:list')")
    @GetMapping("/list")
    @Operation(summary = "查询服务等级列表")
    public TableDataInfo list(ServiceLevel serviceLevel)
    {
        startPage();
        List<ServiceLevelVo> list = serviceLevelService.selectServiceLevelList(serviceLevel);
        return getDataTable(list);
    }

    /**
     * 查询所有启用的服务等级
     */
    @GetMapping("/listAll")
    @Operation(summary = "查询所有启用的服务等级")
    public AjaxResult listAll()
    {
        return success(serviceLevelService.listAll());
    }

    /**
     * 获取服务等级详细信息
     */
    @PreAuthorize("@ss.hasPermi('park:serviceLevel:query')")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取服务等级详细信息")
    public AjaxResult getInfo(@Schema(name = "服务等级ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable("id") Long id)
    {
        return success(serviceLevelService.selectServiceLevelById(id));
    }

    /**
     * 新增服务等级
     */
    @PreAuthorize("@ss.hasPermi('park:serviceLevel:add')")
    @Log(title = "服务等级", businessType = BusinessType.INSERT)
    @PostMapping
    @Operation(summary = "新增服务等级")
    public AjaxResult add(@RequestBody ServiceLevel serviceLevel)
    {
        return toAjax(serviceLevelService.insertServiceLevel(serviceLevel));
    }

    /**
     * 修改服务等级
     */
    @PreAuthorize("@ss.hasPermi('park:serviceLevel:edit')")
    @Log(title = "服务等级", businessType = BusinessType.UPDATE)
    @PutMapping
    @Operation(summary = "修改服务等级")
    public AjaxResult edit(@RequestBody ServiceLevel serviceLevel)
    {
        return toAjax(serviceLevelService.updateServiceLevel(serviceLevel));
    }

    /**
     * 删除服务等级
     */
    @PreAuthorize("@ss.hasPermi('park:serviceLevel:remove')")
    @Log(title = "服务等级", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Operation(summary = "删除服务等级")
    public AjaxResult remove(@Schema(name = "服务等级ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(serviceLevelService.deleteServiceLevelByIds(ids));
    }
}
