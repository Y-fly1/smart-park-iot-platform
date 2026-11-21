package com.zhiwei.park.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiwei.park.domain.Building;
import com.zhiwei.park.domain.vo.CascadeVo;
import com.zhiwei.park.domain.vo.BuildingVo;

import java.util.List;

/**
 * 楼栋Service接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface IBuildingService extends IService<Building>
{
    /**
     * 查询楼栋
     * 
     * @param id 楼栋主键
     * @return 楼栋
     */
    public Building selectBuildingById(Long id);

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
     * 批量删除楼栋
     * 
     * @param ids 需要删除的楼栋主键集合
     * @return 结果
     */
    public int deleteBuildingByIds(Long[] ids);

    /**
     * 删除楼栋信息
     * 
     * @param id 楼栋主键
     * @return 结果
     */
    public int deleteBuildingById(Long id);


    /**
     * 查询负责企业楼栋
     * @return
     */
    List<Building> selectAllByNur();

    /**
     * 根据点位状态查询楼栋-房间-点位级联数据
     *
     * @param status 点位状态，0：未入驻，1：已入驻
     * @return 级联树节点集合
     */
    List<CascadeVo> getRoomAndPointByPointStatus(Integer status);

    List<BuildingVo> getAllBuildingsWithDevice();
}
