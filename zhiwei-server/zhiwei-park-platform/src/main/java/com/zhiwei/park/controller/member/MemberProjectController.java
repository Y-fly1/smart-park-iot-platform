package com.zhiwei.park.controller.member;

import java.util.List;
import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.park.domain.InspectionItem;
import com.zhiwei.park.service.IInspectionItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序巡检项Controller
 *
 * @author ypf
 * @date 2026-10-30
 */
@RestController
@RequestMapping("/member/orders/project")
@Tag(name = "小程序巡检项相关接口")
public class MemberProjectController extends BaseController
{
    @Autowired
    private IInspectionItemService inspectionItemService;

    /**
     * 分页查询巡检项列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询巡检项列表")
    public AjaxResult page(InspectionItem inspectionItem)
    {
        startPage();
        List<InspectionItem> list = inspectionItemService.selectInspectionItemList(inspectionItem);
        return success(getDataTable(list));
    }

    /**
     * 根据编号查询巡检项信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据编号查询巡检项信息")
    public AjaxResult getInfo(@Schema(name = "巡检项编号", requiredMode = Schema.RequiredMode.REQUIRED)
                              @PathVariable("id") Long id)
    {
        return success(inspectionItemService.selectInspectionItemById(id));
    }
}
