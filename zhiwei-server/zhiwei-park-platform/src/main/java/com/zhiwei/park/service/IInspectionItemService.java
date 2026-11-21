package com.zhiwei.park.service;

import java.util.List;
import com.zhiwei.park.domain.InspectionItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiwei.park.domain.vo.InspectionItemVo;

/**
 * 巡检项Service接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface IInspectionItemService extends IService<InspectionItem>
{
    /**
     * 查询巡检项
     * 
     * @param id 巡检项主键
     * @return 巡检项
     */
    public InspectionItem selectInspectionItemById(Long id);

    /**
     * 查询巡检项列表
     * 
     * @param inspectionItem 巡检项
     * @return 巡检项集合
     */
    public List<InspectionItem> selectInspectionItemList(InspectionItem inspectionItem);

    /**
     * 新增巡检项
     * 
     * @param inspectionItem 巡检项
     * @return 结果
     */
    public int insertInspectionItem(InspectionItem inspectionItem);

    /**
     * 修改巡检项
     * 
     * @param inspectionItem 巡检项
     * @return 结果
     */
    public int updateInspectionItem(InspectionItem inspectionItem);

    /**
     * 批量删除巡检项
     * 
     * @param ids 需要删除的巡检项主键集合
     * @return 结果
     */
    public int deleteInspectionItemByIds(Long[] ids);

    /**
     * 删除巡检项信息
     * 
     * @param id 巡检项主键
     * @return 结果
     */
    public int deleteInspectionItemById(Long id);

    /**
     * 查询所有的巡检项
     * @return
     */
    List<InspectionItemVo> listAll();
}
