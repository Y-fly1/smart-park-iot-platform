package com.zhiwei.park.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiwei.park.domain.Building;
import com.zhiwei.park.mapper.BuildingMapper;
import com.zhiwei.park.service.IBuildingService;
import com.zhiwei.park.domain.vo.PointVo;
import com.zhiwei.park.domain.vo.CascadeVo;
import com.zhiwei.park.domain.vo.BuildingVo;
import com.zhiwei.park.domain.vo.RoomVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 楼栋Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements IBuildingService
{
    @Autowired
    private BuildingMapper floorMapper;

    /**
     * 查询楼栋
     * 
     * @param id 楼栋主键
     * @return 楼栋
     */
    @Override
    public Building selectBuildingById(Long id)
    {
        return getById(id);
    }

    /**
     * 新增楼栋
     * 
     * @param building 楼栋
     * @return 结果
     */
    @Override
    public int insertBuilding(Building building)
    {
        return save(building) ? 1 : 0;
    }

    /**
     * 修改楼栋
     * 
     * @param building 楼栋
     * @return 结果
     */
    @Override
    public int updateBuilding(Building building)
    {
        return updateById(building) ? 1 : 0;
    }

    /**
     * 批量删除楼栋
     * 
     * @param ids 需要删除的楼栋主键
     * @return 结果
     */
    @Override
    public int deleteBuildingByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除楼栋信息
     * 
     * @param id 楼栋主键
     * @return 结果
     */
    @Override
    public int deleteBuildingById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }


    /**
     * 查询负责企业楼栋
     * @return
     */
    @Override
    public List<Building> selectAllByNur() {
        return floorMapper.selectAllByNur();
    }

    /**
     * 根据点位状态查询楼栋-房间-点位级联数据
     *
     * @param status 点位状态，0：未入驻，1：已入驻
     * @return 级联树节点集合
     */
    @Override
    public List<CascadeVo> getRoomAndPointByPointStatus(Integer status) {
        List<BuildingVo> floorList = floorMapper.selectBuildingRoomPointByPointStatus(status);

        // 将楼栋-房间-点位数据转换为前端级联组件所需的三级树结构
        List<CascadeVo> tree = new ArrayList<>();
        for (BuildingVo building : floorList) {
            CascadeVo floorNode = new CascadeVo();
            floorNode.setValue(String.valueOf(building.getId()));
            floorNode.setLabel(building.getName());

            List<CascadeVo> roomNodes = new ArrayList<>();
            if (building.getRoomVoList() != null) {
                for (RoomVo room : building.getRoomVoList()) {
                    CascadeVo roomNode = new CascadeVo();
                    roomNode.setValue(String.valueOf(room.getId()));
                    roomNode.setLabel(room.getCode());

                    List<CascadeVo> bedNodes = new ArrayList<>();
                    if (room.getPointVoList() != null) {
                        for (PointVo point : room.getPointVoList()) {
                            CascadeVo bedNode = new CascadeVo();
                            bedNode.setValue(String.valueOf(point.getId()));
                            bedNode.setLabel(point.getPointCode());
                            bedNodes.add(bedNode);
                        }
                    }
                    roomNode.setChildren(bedNodes);
                    roomNodes.add(roomNode);
                }
            }
            floorNode.setChildren(roomNodes);
            tree.add(floorNode);
        }
        return tree;
    }

    @Override
    public List<BuildingVo> getAllBuildingsWithDevice() {
        return floorMapper.getAllBuildingsWithDevice();
    }
}
