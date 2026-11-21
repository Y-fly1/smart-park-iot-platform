package com.zhiwei.park.mapper;

import java.util.List;
import com.zhiwei.park.domain.InspectionItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.vo.InspectionItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 巡检项Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Mapper
public interface InspectionItemMapper extends BaseMapper<InspectionItem>
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
     * 删除巡检项
     * 
     * @param id 巡检项主键
     * @return 结果
     */
    public int deleteInspectionItemById(Long id);

    /**
     * 批量删除巡检项
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInspectionItemByIds(Long[] ids);

    @Select("select id value,name label from inspection_item where status = 1")
    List<InspectionItemVo> listAll();
}
