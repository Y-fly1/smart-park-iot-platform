package com.zhiwei.park.service.impl;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.zhiwei.common.constant.CacheConstants;
import com.zhiwei.common.utils.DateUtils;
import com.zhiwei.common.utils.StringUtils;
import com.zhiwei.park.mapper.DeviceDataMapper;
import com.zhiwei.park.domain.Device;
import com.zhiwei.park.domain.DeviceData;
import com.zhiwei.park.service.IDeviceDataService;
import com.zhiwei.park.service.IDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;

/**
 * 设备数据表Service业务层处理
 * 
 * @author ypf
 * @date 2026-11-03
 */
@Service
public class DeviceDataServiceImpl extends ServiceImpl<DeviceDataMapper, DeviceData> implements IDeviceDataService
{
    private static final Logger log = LoggerFactory.getLogger(DeviceDataServiceImpl.class);

    /** 事件时间格式 */
    private static final DateTimeFormatter EVENT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");

    /** 写入Redis时的JSON配置，统一时间格式 */
    private static final JSONConfig JSON_CONFIG = JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private DeviceDataMapper deviceDataMapper;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 查询设备数据表
     * 
     * @param id 设备数据表主键
     * @return 设备数据表
     */
    @Override
    public DeviceData selectDeviceDataById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询设备数据表列表
     * 
     * @param deviceData 设备数据表
     * @return 设备数据表
     */
    @Override
    public List<DeviceData> selectDeviceDataList(DeviceData deviceData)
    {
        return deviceDataMapper.selectDeviceDataList(deviceData);
    }

    /**
     * 新增设备数据表
     * 
     * @param deviceData 设备数据表
     * @return 结果
     */
    @Override
    public int insertDeviceData(DeviceData deviceData)
    {
        return save(deviceData)? 1 : 0;
    }

    /**
     * 修改设备数据表
     * 
     * @param deviceData 设备数据表
     * @return 结果
     */
    @Override
    public int updateDeviceData(DeviceData deviceData)
    {
        return updateById(deviceData)? 1 : 0;
    }

    /**
     * 批量删除设备数据表
     * 
     * @param ids 需要删除的设备数据表主键
     * @return 结果
     */
    @Override
    public int deleteDeviceDataByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除设备数据表信息
     * 
     * @param id 设备数据表主键
     * @return 结果
     */
    @Override
    public int deleteDeviceDataById(Long id)
    {
        return removeById(id)? 1 : 0;
    }

    /**
     * 保存IOTDA平台上报的设备数据
     *
     * @param contentStr 设备上报消息内容
     */
    @Override
    public void saveReportedData(String contentStr)
    {
        if (StringUtils.isEmpty(contentStr)) {
            return;
        }
        //1. 解析消息内容，取出设备ID、服务属性、上报时间
        JSONObject root = JSONUtil.parseObj(contentStr);
        JSONObject notifyData = root.getJSONObject("notify_data");
        if (notifyData == null) {
            return;
        }
        JSONObject header = notifyData.getJSONObject("header");
        JSONObject body = notifyData.getJSONObject("body");
        if (header == null || body == null) {
            return;
        }
        String deviceId = header.getStr("device_id");
        JSONArray services = body.getJSONArray("services");
        if (StringUtils.isEmpty(deviceId) || CollUtil.isEmpty(services)) {
            return;
        }

        //2. 根据设备ID查询设备，设备不存在直接返回
        Device device = deviceService.lambdaQuery().eq(Device::getIotId, deviceId).one();
        if (device == null) {
            return;
        }

        //3. 按属性批量组装设备数据
        List<DeviceData> dataList = new ArrayList<>();
        for (int i = 0; i < services.size(); i++) {
            JSONObject service = services.getJSONObject(i);
            JSONObject properties = service.getJSONObject("properties");
            if (properties == null || properties.isEmpty()) {
                continue;
            }
            String eventTime = service.getStr("event_time");
            if (StringUtils.isEmpty(eventTime)) {
                eventTime = root.getStr("event_time");
            }
            LocalDateTime alarmTime = parseEventTime(eventTime);
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                DeviceData data = new DeviceData();
                data.setDeviceName(device.getDeviceName());
                data.setIotId(deviceId);
                data.setProductKey(device.getProductKey());
                data.setProductName(device.getProductName());
                data.setFunctionId(entry.getKey());
                data.setAccessLocation(device.getBindingLocation());
                data.setLocationType(device.getLocationType());
                data.setPhysicalLocationType(device.getPhysicalLocationType());
                data.setDeviceDescription(device.getDeviceDescription());
                data.setDataValue(String.valueOf(entry.getValue()));
                data.setAlarmTime(alarmTime == null ? null : DateUtils.toDate(alarmTime));
                dataList.add(data);
            }
        }
        if (CollUtil.isEmpty(dataList)) {
            return;
        }

        //4. 批量保存设备数据
        saveBatch(dataList);

        //5. 将本次上报的数据写入Redis Hash，小key为设备ID，保证每个设备只保留最新一次上报数据
        try {
            redisTemplate.opsForHash().put(CacheConstants.IOT_DEVICE_DATA_LIST, deviceId, JSONUtil.toJsonStr(dataList, JSON_CONFIG));
        } catch (Exception e) {
            //Redis写入失败不影响数据库中的数据保存
            log.error("设备上报数据写入Redis失败，设备ID：{}", deviceId, e);
        }
    }

    /**
     * 将世界时间转为北京时间
     *
     * @param eventTime 世界时间字符串，如 20260606T111615Z
     * @return 北京时间
     */
    private LocalDateTime parseEventTime(String eventTime)
    {
        if (StringUtils.isEmpty(eventTime)) {
            return null;
        }
        try {
            return OffsetDateTime.parse(eventTime, EVENT_TIME_FORMATTER)
                    .atZoneSameInstant(ZoneId.of("Asia/Shanghai"))
                    .toLocalDateTime();
        } catch (Exception e) {
            log.warn("设备上报时间格式错误：{}", eventTime, e);
            return null;
        }
    }
}
