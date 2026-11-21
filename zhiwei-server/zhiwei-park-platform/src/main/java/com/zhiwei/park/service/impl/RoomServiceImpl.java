package com.zhiwei.park.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiwei.common.constant.CacheConstants;
import com.zhiwei.common.utils.StringUtils;
import com.zhiwei.park.domain.Device;
import com.zhiwei.park.domain.DeviceData;
import com.zhiwei.park.domain.Room;
import com.zhiwei.park.mapper.DeviceMapper;
import com.zhiwei.park.mapper.RoomMapper;
import com.zhiwei.park.service.IRoomService;
import com.zhiwei.park.domain.vo.PointVo;
import com.zhiwei.park.domain.vo.DeviceVo;
import com.zhiwei.park.domain.vo.RoomVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房间Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {
    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 查询房间
     *
     * @param id 房间主键
     * @return 房间
     */
    @Override
    public Room selectRoomById(Long id) {
        return getById(id);
    }

    /**
     * 查询房间列表
     *
     * @param room 房间
     * @return 房间
     */
    @Override
    public List<Room> selectRoomList(Room room) {
        return roomMapper.selectRoomList(room);
    }

    /**
     * 新增房间
     *
     * @param room 房间
     * @return 结果
     */
    @Override
    public int insertRoom(Room room) {
        return save(room) ? 1 : 0;
    }

    /**
     * 修改房间
     *
     * @param room 房间
     * @return 结果
     */
    @Override
    public int updateRoom(Room room) {
        return updateById(room) ? 1 : 0;
    }

    /**
     * 批量删除房间
     *
     * @param ids 需要删除的房间主键
     * @return 结果
     */
    @Override
    public int deleteRoomByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 根据楼栋 id 获取房间视图对象列表
     *
     * @param buildingId
     * @return
     */
    @Override
    public List<RoomVo> getRoomsByBuildingId(Long buildingId) {
        return roomMapper.selectByBuildingId(buildingId);
    }


    /**
     * 获取所有房间（负责企业）
     *
     * @param buildingId
     * @return
     */
    @Override
    public List<RoomVo> getRoomsWithNurByBuildingId(Long buildingId) {
        return roomMapper.selectByBuildingIdWithNur(buildingId);
    }

    /**
     * 查询房间详情（楼栋、房间、价格）
     *
     * @param id 房间主键
     * @return 房间视图对象
     */
    @Override
    public RoomVo getRoomDetailById(Long id) {
        return roomMapper.selectRoomDetailById(id);
    }

    /**
     * 获取楼栋下带设备的房间及设备最新数据
     *
     * @param buildingId 楼栋ID
     * @return 房间视图对象集合
     */
    @Override
    public List<RoomVo> getRoomsWithDeviceByBuildingId(Long buildingId) {
        //1. 查询楼栋下存在设备的房间、点位及入驻企业
        List<RoomVo> rooms = roomMapper.selectByBuildingIdWithDevice(buildingId);
        if (CollUtil.isEmpty(rooms)) {
            return rooms;
        }

        //2. 收集房间ID与点位ID
        List<Long> roomIds = new ArrayList<>();
        List<Long> pointIds = new ArrayList<>();
        for (RoomVo room : rooms) {
            roomIds.add(room.getId());
            if (CollUtil.isNotEmpty(room.getPointVoList())) {
                for (PointVo point : room.getPointVoList()) {
                    pointIds.add(point.getId());
                }
            }
        }

        //3. 查询房间与点位绑定的设备，并按绑定位置分组
        List<Device> devices = deviceMapper.selectDevicesByRoomAndPoint(roomIds, pointIds);
        Map<String, List<DeviceVo>> deviceMap = new HashMap<>();
        if (CollUtil.isNotEmpty(devices)) {
            for (Device device : devices) {
                deviceMap.computeIfAbsent(device.getBindingLocation(), key -> new ArrayList<>())
                        .add(buildDeviceVo(device));
            }
        }

        //4. 为房间和点位填充设备，没有设备的返回空集合
        for (RoomVo room : rooms) {
            room.setDeviceVos(deviceMap.getOrDefault(String.valueOf(room.getId()), Collections.emptyList()));
            if (CollUtil.isEmpty(room.getPointVoList())) {
                room.setPointVoList(Collections.emptyList());
                continue;
            }
            for (PointVo point : room.getPointVoList()) {
                point.setDeviceVos(deviceMap.getOrDefault(String.valueOf(point.getId()), Collections.emptyList()));
            }
        }
        return rooms;
    }

    /**
     * 组装设备VO，并从Redis中读取设备最新上报数据
     *
     * @param device 设备
     * @return 设备VO
     */
    private DeviceVo buildDeviceVo(Device device) {
        DeviceVo vo = new DeviceVo();
        BeanUtils.copyProperties(device, vo);
        vo.setDeviceDataVos(getLatestDeviceData(device.getIotId()));
        return vo;
    }

    /**
     * 从Redis中读取设备最新上报数据
     *
     * @param iotId 设备ID
     * @return 设备数据集合
     */
    private List<DeviceData> getLatestDeviceData(String iotId) {
        if (StringUtils.isEmpty(iotId)) {
            return Collections.emptyList();
        }
        Object json = redisTemplate.opsForHash().get(CacheConstants.IOT_DEVICE_DATA_LIST, iotId);
        if (json == null || StringUtils.isEmpty(json.toString())) {
            return Collections.emptyList();
        }
        return JSONUtil.toList(json.toString(), DeviceData.class);
    }

}
