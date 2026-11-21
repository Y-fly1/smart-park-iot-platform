package com.zhiwei.park.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import com.zhiwei.park.domain.InspectionItem;
import com.zhiwei.park.service.IInspectionItemService;
import com.zhiwei.common.utils.poi.ExcelUtil;
import com.zhiwei.common.core.page.TableDataInfo;

/**
 * 巡检项Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/park/inspectionItem")
@Tag(name = "巡检项相关接口")
public class InspectionItemController extends BaseController
{
    @Autowired
    private IInspectionItemService inspectionItemService;

    @GetMapping("/all")
    @Operation(summary = "查询所有巡检项")
    public AjaxResult listAll(){
        return success(inspectionItemService.listAll());
    }

    /**
     * 查询巡检项列表
     */
    @Operation(summary = "查询巡检项列表")
    @PreAuthorize("@ss.hasPermi('park:inspectionItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionItem inspectionItem)
    {
        startPage();
        List<InspectionItem> list = inspectionItemService.selectInspectionItemList(inspectionItem);
        return getDataTable(list);
    }

    /**
     * 导出巡检项列表
     */
    @Operation(summary = "导出巡检项列表")
    @PreAuthorize("@ss.hasPermi('park:inspectionItem:export')")
    @Log(title = "巡检项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InspectionItem inspectionItem)
    {
        List<InspectionItem> list = inspectionItemService.selectInspectionItemList(inspectionItem);
        ExcelUtil<InspectionItem> util = new ExcelUtil<InspectionItem>(InspectionItem.class);
        util.exportExcel(response, list, "巡检项数据");
    }

    /**
     * 获取巡检项详细信息
     */
    @Operation(summary = "获取巡检项详细信息")
    @PreAuthorize("@ss.hasPermi('park:inspectionItem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@Schema(name = "巡检项ID", requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("id") Long id)
    {
        return success(inspectionItemService.selectInspectionItemById(id));
    }

    /**
     * 新增巡检项
     */
    @Operation(summary = "新增巡检项")
    @PreAuthorize("@ss.hasPermi('park:inspectionItem:add')")
    @Log(title = "巡检项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionItem inspectionItem)
    {
        return toAjax(inspectionItemService.insertInspectionItem(inspectionItem));
    }

    /**
     * 修改巡检项
     */
    @Operation(summary = "修改巡检项")
    @PreAuthorize("@ss.hasPermi('park:inspectionItem:edit')")
    @Log(title = "巡检项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionItem inspectionItem)
    {
        return toAjax(inspectionItemService.updateInspectionItem(inspectionItem));
    }

    /**
     * 删除巡检项
     */
    @Operation(summary = "删除巡检项")
    @PreAuthorize("@ss.hasPermi('park:inspectionItem:remove')")
    @Log(title = "巡检项", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@Schema(name = "巡检项ID", requiredMode = Schema.RequiredMode.REQUIRED) @PathVariable Long[] ids)
    {
        return toAjax(inspectionItemService.deleteInspectionItemByIds(ids));
    }
}
