package com.zhiwei.park.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.Building;
import com.zhiwei.park.domain.vo.BuildingVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 楼栋Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Mapper
public interface BuildingMapper extends BaseMapper<Building>
{
    /**
     * 查询楼栋
     * 
     * @param id 楼栋主键
     * @return 楼栋
     */
    public Building selectBuildingById(Long id);

    /**
     * 查询楼栋列表
     * 
     * @param building 楼栋
     * @return 楼栋集合
     */
    public List<Building> selectBuildingList(Building building);

    /**
     * 新增楼栋
     * 
     * @param building 楼栋
     * @return 结果
     */
    public int insertBuilding(Building building);

    /**
     * 修改楼栋
     * 
     * @param building 楼栋
     * @return 结果
     */
    public int updateBuilding(Building building);

    /**
     * 删除楼栋
     * 
     * @param id 楼栋主键
     * @return 结果
     */
    public int deleteBuildingById(Long id);

    /**
     * 批量删除楼栋
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBuildingByIds(Long[] ids);

    List<Building> selectAllByNur();

    /**
     * 根据点位状态查询楼栋（含房间、点位）
     *
     * @param status 点位状态，0：未入驻，1：已入驻
     * @return 楼栋视图对象集合
     */
    List<BuildingVo> selectBuildingRoomPointByPointStatus(Integer status);

    List<BuildingVo> getAllBuildingsWithDevice();
}
