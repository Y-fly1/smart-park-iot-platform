package com.zhiwei.park.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiwei.park.domain.Device;

/**
 * 设备表Mapper接口
 * 
 * @author ypf
 * @date 2026-11-02
 */
@Mapper
public interface DeviceMapper extends BaseMapper<Device>
{
    /**
     * 查询设备表
     * 
     * @param id 设备表主键
     * @return 设备表
     */
    public Device selectDeviceById(Long id);

    /**
     * 查询设备表列表
     * 
     * @param device 设备表
     * @return 设备表集合
     */
    public List<Device> selectDeviceList(Device device);

    /**
     * 新增设备表
     * 
     * @param device 设备表
     * @return 结果
     */
    public int insertDevice(Device device);

    /**
     * 修改设备表
     * 
     * @param device 设备表
     * @return 结果
     */
    public int updateDevice(Device device);

    /**
     * 删除设备表
     * 
     * @param id 设备表主键
     * @return 结果
     */
    public int deleteDeviceById(Long id);

    /**
     * 批量删除设备表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceByIds(Long[] ids);

    /**
     * 查询房间与点位绑定的固定设备
     *
     * @param roomIds 房间ID集合
     * @param pointIds  点位ID集合
     * @return 设备集合
     */
    List<Device> selectDevicesByRoomAndPoint(@Param("roomIds") List<Long> roomIds, @Param("pointIds") List<Long> pointIds);
}
