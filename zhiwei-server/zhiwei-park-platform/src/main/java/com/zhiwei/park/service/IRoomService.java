package com.zhiwei.park.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiwei.park.domain.Room;
import com.zhiwei.park.domain.vo.RoomVo;

import java.util.List;

/**
 * 房间Service接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface IRoomService extends IService<Room>
{
    /**
     * 查询房间
     * 
     * @param id 房间主键
     * @return 房间
     */
    public Room selectRoomById(Long id);

    /**
     * 查询房间列表
     * 
     * @param room 房间
     * @return 房间集合
     */
    public List<Room> selectRoomList(Room room);

    /**
     * 新增房间
     * 
     * @param room 房间
     * @return 结果
     */
    public int insertRoom(Room room);

    /**
     * 修改房间
     * 
     * @param room 房间
     * @return 结果
     */
    public int updateRoom(Room room);

    /**
     * 批量删除房间
     * 
     * @param ids 需要删除的房间主键集合
     * @return 结果
     */
    public int deleteRoomByIds(Long[] ids);

    /**
     * 根据楼栋 id 获取房间视图对象列表
     * @param buildingId
     * @return
     */
    List<RoomVo> getRoomsByBuildingId(Long buildingId);

    /**
     * 获取所有房间（负责企业）
     * @param buildingId
     * @return
     */
    List<RoomVo> getRoomsWithNurByBuildingId(Long buildingId);

    /**
     * 查询房间详情（楼栋、房间、价格）
     *
     * @param id 房间主键
     * @return 房间视图对象
     */
    RoomVo getRoomDetailById(Long id);

    /**
     * 获取楼栋下带设备的房间及设备最新数据
     *
     * @param buildingId 楼栋ID
     * @return 房间视图对象集合
     */
    List<RoomVo> getRoomsWithDeviceByBuildingId(Long buildingId);

}
