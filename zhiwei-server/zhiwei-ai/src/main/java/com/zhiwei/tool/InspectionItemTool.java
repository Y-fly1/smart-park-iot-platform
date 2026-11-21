package com.zhiwei.tool;

import com.zhiwei.park.domain.vo.InspectionItemVo;
import com.zhiwei.park.service.IInspectionItemService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InspectionItemTool {

    @Autowired
    private IInspectionItemService iInspectionItemService;

    @Tool(name = "find_project_tool", description = "查询所有巡检项")
    public List<InspectionItemVo> findAllInspectionItem(){

        List<InspectionItemVo> inspectionItemVos = iInspectionItemService.listAll();
        return inspectionItemVos;
    }
}
