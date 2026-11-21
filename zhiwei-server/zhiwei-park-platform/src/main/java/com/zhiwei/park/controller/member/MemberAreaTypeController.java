package com.zhiwei.park.controller.member;

import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.park.domain.AreaType;
import com.zhiwei.park.service.IAreaTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member/areaTypes")
@Tag(name = "客户区域类型管理")
public class MemberAreaTypeController extends BaseController {

    @Autowired
    private IAreaTypeService areaTypeService;

    @GetMapping
    @Operation(summary = "根据状态查询区域类型")
    public AjaxResult findAreaTypeListByStatus(Integer status) {

        List<AreaType> areaTypeVoList = areaTypeService.findAreaTypeListByStatus(status);
        return success(areaTypeVoList);
    }

}
