package com.zhiwei.park.service.impl;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.AddDevice;
import com.huaweicloud.sdk.iotda.v5.model.AddDeviceRequest;
import com.huaweicloud.sdk.iotda.v5.model.AddDeviceResponse;
import com.huaweicloud.sdk.iotda.v5.model.AuthInfo;
import com.huaweicloud.sdk.iotda.v5.model.DeleteDeviceRequest;
import com.huaweicloud.sdk.iotda.v5.model.DeleteDeviceResponse;
import com.huaweicloud.sdk.iotda.v5.model.DeviceShadowData;
import com.huaweicloud.sdk.iotda.v5.model.DeviceShadowProperties;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsRequest;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsResponse;
import com.huaweicloud.sdk.iotda.v5.model.ServiceCapability;
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceRequest;
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceResponse;
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceShadowRequest;
import com.huaweicloud.sdk.iotda.v5.model.ShowDeviceShadowResponse;
import com.huaweicloud.sdk.iotda.v5.model.ShowProductRequest;
import com.huaweicloud.sdk.iotda.v5.model.ShowProductResponse;
import com.huaweicloud.sdk.iotda.v5.model.UpdateDevice;
import com.huaweicloud.sdk.iotda.v5.model.UpdateDeviceRequest;
import com.huaweicloud.sdk.iotda.v5.model.UpdateDeviceResponse;
import com.zhiwei.common.constant.CacheConstants;
import com.zhiwei.common.exception.ServiceException;
import com.zhiwei.common.exception.base.BaseException;
import com.zhiwei.common.utils.DateUtils;
import com.zhiwei.common.utils.StringUtils;
import com.zhiwei.park.domain.dto.DeviceRegisterDto;
import com.zhiwei.park.domain.dto.DeviceUpdateDto;
import com.zhiwei.park.domain.vo.DeviceDetailVo;
import com.zhiwei.park.domain.vo.DeviceReportedDataVo;
import com.zhiwei.park.domain.vo.ProductVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhiwei.park.mapper.DeviceMapper;
import com.zhiwei.park.domain.Device;
import com.zhiwei.park.service.IDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Arrays;

/**
 * 设备表Service业务层处理
 * 
 * @author ypf
 * @date 2026-11-02
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService
{
    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private IoTDAClient client;


    /**
     * 查询设备表
     * 
     * @param id 设备表主键
     * @return 设备表
     */
    @Override
    public Device selectDeviceById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询设备表列表
     * 
     * @param device 设备表
     * @return 设备表
     */
    @Override
    public List<Device> selectDeviceList(Device device)
    {
        return deviceMapper.selectDeviceList(device);
    }

    /**
     * 新增设备表
     * 
     * @param device 设备表
     * @return 结果
     */
    @Override
    public int insertDevice(Device device)
    {
        return save(device)? 1 : 0;
    }

    /**
     * 修改设备表
     * 
     * @param device 设备表
     * @return 结果
     */
    @Override
    public int updateDevice(Device device)
    {
        return updateById(device)? 1 : 0;
    }

    /**
     * 批量删除设备表
     * 
     * @param ids 需要删除的设备表主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids))? 1 : 0;
    }

    /**
     * 删除设备表信息
     * 
     * @param id 设备表主键
     * @return 结果
     */
    @Override
    public int deleteDeviceById(Long id)
    {
        return removeById(id)? 1 : 0;
    }

    /**
     * 注册设备（华为云IoT）
     *
     * @param dto 设备注册信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerDevice(DeviceRegisterDto dto) {
        //1. 校验设备名称是否重复
        if (lambdaQuery().eq(Device::getDeviceName, dto.getDeviceName()).count() > 0) {
            throw new ServiceException("设备名称已存在：" + dto.getDeviceName());
        }
        //2. 校验设备标识符是否重复
        if (lambdaQuery().eq(Device::getNodeId, dto.getNodeId()).count() > 0) {
            throw new ServiceException("设备标识符已存在：" + dto.getNodeId());
        }
        //3. 校验同一位置是否已绑定相同产品
        boolean bound = lambdaQuery()
                .eq(Device::getBindingLocation, dto.getBindingLocation())
                .eq(Device::getLocationType, dto.getLocationType())
                .eq(Device::getPhysicalLocationType, dto.getPhysicalLocationType())
                .eq(Device::getProductKey, dto.getProductKey())
                .count() > 0;
        if (bound) {
            throw new ServiceException("该位置已绑定相同产品，请勿重复绑定");
        }

        //4. 生成设备秘钥并在华为云IoT注册设备
        String secret = IdUtil.simpleUUID();
        AddDevice addDevice = new AddDevice()
                .withDeviceName(dto.getDeviceName())
                .withNodeId(dto.getNodeId())
                .withProductId(dto.getProductKey())
                .withAuthInfo(new AuthInfo().withAuthType("SECRET").withSecret(secret));
        AddDeviceResponse response;
        try {
            response = client.addDevice(new AddDeviceRequest().withBody(addDevice));
        } catch (Exception e) {
            throw new ServiceException("华为云IoT设备注册失败：" + e.getMessage());
        }
        if (response.getHttpStatusCode() != 201) {
            throw new ServiceException("华为云IoT设备注册失败，状态码：" + response.getHttpStatusCode());
        }

        //5. 保存设备数据到本地设备表
        Device device = new Device();
        BeanUtils.copyProperties(dto, device);
        device.setIotId(response.getDeviceId());
        device.setSecret(secret);
        device.setHaveEntranceGuard(0);
        if (!save(device)) {
            throw new ServiceException("设备注册成功，但设备数据保存失败");
        }
    }

    /**
     * 查询设备详细信息
     *
     * @param iotId 设备ID
     * @return 设备详细信息
     */
    @Override
    public DeviceDetailVo getDeviceDetail(String iotId) {
        //1. 先根据设备ID查询本地设备数据
        Device device = lambdaQuery().eq(Device::getIotId, iotId).one();
        if (device == null) {
            throw new ServiceException("设备不存在：" + iotId);
        }

        //2. 本地数据不够完整，调用华为云查询设备详情补充
        ShowDeviceResponse response;
        try {
            response = client.showDevice(new ShowDeviceRequest().withDeviceId(iotId));
        } catch (Exception e) {
            throw new ServiceException("查询华为云设备详情失败：" + e.getMessage());
        }
        if (response.getHttpStatusCode() != 200) {
            throw new ServiceException("查询华为云设备详情失败，状态码：" + response.getHttpStatusCode());
        }

        //3. 合并本地与云端数据，本地有值时优先使用本地数据
        DeviceDetailVo vo = new DeviceDetailVo();
        BeanUtils.copyProperties(device, vo);
        vo.setDeviceStatus(response.getStatus());
        if (StringUtils.isEmpty(vo.getProductKey())) {
            vo.setProductKey(response.getProductId());
        }
        if (StringUtils.isEmpty(vo.getProductName())) {
            vo.setProductName(response.getProductName());
        }

        //4. 激活时间转为LocalDateTime类型的北京时间
        String activeTime = response.getActiveTime();
        if (StringUtils.isNotEmpty(activeTime)) {
            vo.setActiveTime(OffsetDateTime.parse(activeTime).atZoneSameInstant(ZoneId.of("Asia/Shanghai"))
                    .toLocalDateTime());
        }
        return vo;
    }

    /**
     * 修改设备信息（本地与华为云IoT）
     *
     * @param dto 设备修改信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceInfo(DeviceUpdateDto dto) {
        //1. 校验设备是否存在
        Device device = getById(dto.getId());
        if (device == null) {
            throw new ServiceException("设备不存在：" + dto.getId());
        }
        //2. 校验同一位置是否已绑定相同产品（排除当前设备）
        boolean bound = lambdaQuery()
                .eq(Device::getBindingLocation, dto.getBindingLocation())
                .eq(Device::getLocationType, dto.getLocationType())
                .eq(Device::getPhysicalLocationType, dto.getPhysicalLocationType())
                .eq(Device::getProductKey, dto.getProductKey())
                .ne(Device::getId, dto.getId())
                .count() > 0;
        if (bound) {
            throw new ServiceException("该位置已绑定相同产品，请勿重复绑定");
        }

        //3. 修改本地设备数据
        Device update = new Device();
        BeanUtils.copyProperties(dto, update);
        if (!updateById(update)) {
            throw new ServiceException("修改本地设备数据失败");
        }

        //4. 修改华为云IoT设备信息（平台仅支持修改设备名称，产品与节点创建后不可改）
        UpdateDeviceResponse response;
        try {
            response = client.updateDevice(new UpdateDeviceRequest()
                    .withDeviceId(device.getIotId())
                    .withBody(new UpdateDevice().withDeviceName(dto.getDeviceName())));
        } catch (Exception e) {
            throw new ServiceException("修改华为云IoT设备信息失败：" + e.getMessage());
        }
        if (response.getHttpStatusCode() != 200) {
            throw new ServiceException("修改华为云IoT设备信息失败，状态码：" + response.getHttpStatusCode());
        }
    }

    /**
     * 删除设备（本地与华为云IoT）
     *
     * @param iotId 设备ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDeviceInfo(String iotId) {
        //1. 校验设备是否存在
        Device device = lambdaQuery().eq(Device::getIotId, iotId).one();
        if (device == null) {
            throw new ServiceException("设备不存在：" + iotId);
        }

        //2. 先删除本地设备数据
        if (!removeById(device.getId())) {
            throw new ServiceException("删除本地设备数据失败");
        }

        //3. 再删除华为云IoT设备
        DeleteDeviceResponse response;
        try {
            response = client.deleteDevice(new DeleteDeviceRequest().withDeviceId(iotId));
        } catch (Exception e) {
            throw new ServiceException("删除华为云IoT设备失败：" + e.getMessage());
        }
        if (response.getHttpStatusCode() != 204) {
            throw new ServiceException("删除华为云IoT设备失败，状态码：" + response.getHttpStatusCode());
        }
    }

    /**
     * 查询设备上报的数据
     *
     * @param iotId 设备ID
     * @return 设备上报数据列表
     */
    @Override
    public List<DeviceReportedDataVo> queryServiceProperties(String iotId) {
        //1. 调用华为云查询设备影子数据
        ShowDeviceShadowResponse response;
        try {
            response = client.showDeviceShadow(new ShowDeviceShadowRequest().withDeviceId(iotId));
        } catch (Exception e) {
            throw new ServiceException("查询华为云设备上报数据失败：" + e.getMessage());
        }
        if (response.getHttpStatusCode() != 200) {
            throw new ServiceException("查询华为云设备上报数据失败，状态码：" + response.getHttpStatusCode());
        }

        //2. 按接口文档组装数据：每个服务下的属性拆成 {functionId, eventTime, value}
        List<DeviceReportedDataVo> result = new ArrayList<>();
        List<DeviceShadowData> shadowList = response.getShadow();
        if (shadowList == null || shadowList.isEmpty()) {
            return result;
        }
        for (DeviceShadowData shadow : shadowList) {
            DeviceShadowProperties reported = shadow.getReported();
            if (reported == null || reported.getProperties() == null) {
                continue;
            }
            //3. 上报时间由世界时间转为LocalDateTime类型的北京时间
            LocalDateTime eventTime = null;
            String eventTimeStr = reported.getEventTime();
            if (StringUtils.isNotEmpty(eventTimeStr)) {
                eventTime = OffsetDateTime.parse(eventTimeStr, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX"))
                        .atZoneSameInstant(ZoneId.of("Asia/Shanghai"))
                        .toLocalDateTime();
            }
            JSONObject properties = JSONUtil.parseObj(reported.getProperties());
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                DeviceReportedDataVo vo = new DeviceReportedDataVo();
                vo.setFunctionId(entry.getKey());
                vo.setEventTime(eventTime);
                vo.setValue(entry.getValue());
                result.add(vo);
            }
        }
        return result;
    }

    /**
     * 同步产品列表
     */
    @Override
    public void syncProductList() {
        //请求参数
        ListProductsRequest listProductsRequest = new ListProductsRequest();
        //设置条数
        listProductsRequest.setLimit(50);
        //发起请求
        ListProductsResponse response = client.listProducts(listProductsRequest);
        if(response.getHttpStatusCode() != 200){
            throw new BaseException("物联网接口 - 查询产品，同步失败");
        }
        //存储到redis
        redisTemplate.opsForValue().set(CacheConstants.IOT_ALL_PRODUCT_LIST, JSONUtil.toJsonStr(response.getProducts()));

    }

    /**
     * 查询所有产品列表
     *
     * @return
     */
    @Override
    public List<ProductVo> allProduct() {
        //从redis中查询数据
        String jsonStr = redisTemplate.opsForValue().get(CacheConstants.IOT_ALL_PRODUCT_LIST);
        //如果数据为空，则返回一个空集合
        if(StringUtils.isEmpty(jsonStr)){
            return Collections.emptyList();
        }
        //解析数据，并返回
        return JSONUtil.toList(jsonStr, ProductVo.class);
    }

    /**
     * 查询产品详情
     *
     * @param productId 产品ID
     * @return 产品下包含的服务能力列表
     */
    @Override
    public List<ServiceCapability> queryProduct(String productId) {
        //1. 参数校验
        if (StringUtils.isEmpty(productId)) {
            throw new BaseException("请输入正确的参数");
        }

        //2. 调用华为云查询产品详情
        ShowProductResponse response;
        try {
            response = client.showProduct(new ShowProductRequest().withProductId(productId));
        } catch (Exception e) {
            throw new BaseException("查询产品详情失败：" + e.getMessage());
        }

        //3. 返回服务能力列表，没有服务时返回空集合
        List<ServiceCapability> serviceCapabilities = response.getServiceCapabilities();
        return serviceCapabilities == null ? Collections.emptyList() : serviceCapabilities;
    }
}
