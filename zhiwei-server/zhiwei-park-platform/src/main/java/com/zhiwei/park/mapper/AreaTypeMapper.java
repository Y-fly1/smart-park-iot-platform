package com.zhiwei.park.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.AreaType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 区域类型Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Mapper
public interface AreaTypeMapper extends BaseMapper<AreaType>
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
     * 删除区域类型
     * 
     * @param id 区域类型主键
     * @return 结果
     */
    public int deleteAreaTypeById(Long id);

    /**
     * 批量删除区域类型
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAreaTypeByIds(Long[] ids);
}
