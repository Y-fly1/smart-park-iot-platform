package com.zhiwei.park.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.Room;
import com.zhiwei.park.domain.vo.RoomVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 房间Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room>
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
     * 删除房间
     * 
     * @param id 房间主键
     * @return 结果
     */
    public int deleteRoomById(Long id);

    /**
     * 批量删除房间
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRoomByIds(Long[] ids);

    List<RoomVo> selectByBuildingId(Long buildingId);

    List<RoomVo> selectByBuildingIdWithNur(Long buildingId);

    /**
     * 查询楼栋下存在设备的房间（含点位及入驻企业）
     *
     * @param buildingId 楼栋ID
     * @return 房间视图对象集合
     */
    List<RoomVo> selectByBuildingIdWithDevice(Long buildingId);

    /**
     * 查询房间详情（楼栋、房间、价格）
     *
     * @param id 房间主键
     * @return 房间视图对象
     */
    RoomVo selectRoomDetailById(Long id);

}
