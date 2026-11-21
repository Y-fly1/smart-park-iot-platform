package com.zhiwei.park.service;

import java.util.List;
import com.huaweicloud.sdk.iotda.v5.model.ServiceCapability;
import com.zhiwei.park.domain.Device;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiwei.park.domain.dto.DeviceRegisterDto;
import com.zhiwei.park.domain.dto.DeviceUpdateDto;
import com.zhiwei.park.domain.vo.DeviceDetailVo;
import com.zhiwei.park.domain.vo.DeviceReportedDataVo;
import com.zhiwei.park.domain.vo.ProductVo;

/**
 * 设备表Service接口
 * 
 * @author ypf
 * @date 2026-11-02
 */
public interface IDeviceService extends IService<Device>
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
     * 批量删除设备表
     * 
     * @param ids 需要删除的设备表主键集合
     * @return 结果
     */
    public int deleteDeviceByIds(Long[] ids);

    /**
     * 删除设备表信息
     * 
     * @param id 设备表主键
     * @return 结果
     */
    public int deleteDeviceById(Long id);

    /**
     * 注册设备（华为云IoT）
     *
     * @param dto 设备注册信息
     */
    void registerDevice(DeviceRegisterDto dto);

    /**
     * 查询设备详细信息
     *
     * @param iotId 设备ID
     * @return 设备详细信息
     */
    DeviceDetailVo getDeviceDetail(String iotId);

    /**
     * 修改设备信息（本地与华为云IoT）
     *
     * @param dto 设备修改信息
     */
    void updateDeviceInfo(DeviceUpdateDto dto);

    /**
     * 删除设备（本地与华为云IoT）
     *
     * @param iotId 设备ID
     */
    void deleteDeviceInfo(String iotId);

    /**
     * 查询设备上报的数据
     *
     * @param iotId 设备ID
     * @return 设备上报数据列表
     */
    List<DeviceReportedDataVo> queryServiceProperties(String iotId);

    /**
     * 从物联网平台同步产品列表
     */
    void syncProductList();

    /**
     * 查询所有产品列表
     *
     * @return 产品列表
     */
    List<ProductVo> allProduct();

    /**
     * 查询产品详情
     *
     * @param productId 产品ID
     * @return 产品下包含的服务能力列表
     */
    List<ServiceCapability> queryProduct(String productId);
}
