package com.zhiwei.park.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiwei.park.domain.AreaType;

import java.util.List;

/**
 * 区域类型Service接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface IAreaTypeService extends IService<AreaType>
{
    /**
     * 查询区域类型
     * 
     * @param id 区域类型主键
     * @return 区域类型
     */
    public AreaType selectAreaTypeById(Long id);

    /**
     * 查询区域类型列表
     * 
     * @param areaType 区域类型
     * @return 区域类型集合
     */
    public List<AreaType> selectAreaTypeList(AreaType areaType);

    /**
     * 新增区域类型
     * 
     * @param areaType 区域类型
     * @return 结果
     */
    public int insertAreaType(AreaType areaType);

    /**
     * 修改区域类型
     * 
     * @param areaType 区域类型
     * @return 结果
     */
    public int updateAreaType(AreaType areaType);

    /**
     * 批量删除区域类型
     * 
     * @param ids 需要删除的区域类型主键集合
     * @return 结果
     */
    public int deleteAreaTypeByIds(Long[] ids);

    /**
     * 删除区域类型信息
     * 
     * @param id 区域类型主键
     * @return 结果
     */
    public int deleteAreaTypeById(Long id);

    /**
     * 按照状态查询房间类型
     * @param status
     * @return
     */
    List<AreaType> findAreaTypeListByStatus(Integer status);
}
