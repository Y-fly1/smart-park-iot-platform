package com.zhiwei.park.controller;

import com.zhiwei.common.core.controller.BaseController;
import com.zhiwei.common.core.domain.AjaxResult;
import com.zhiwei.common.core.page.TableDataInfo;
import com.zhiwei.park.domain.Device;
import com.zhiwei.park.domain.dto.DeviceRegisterDto;
import com.zhiwei.park.domain.dto.DeviceUpdateDto;
import com.zhiwei.park.service.IDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 设备Controller
 *
 * @author ruoyi
 * @date 2026-04-05
 */
@RestController
@RequestMapping("/park/device")
@Tag(name = "设备相关接口")
public class DeviceController extends BaseController
{
    @Autowired
    private IDeviceService deviceService;

    /**
     * 查询设备列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询设备列表")
    public TableDataInfo list(Device device)
    {
        startPage();
        List<Device> list = deviceService.selectDeviceList(device);
        return getDataTable(list);
    }


    /**
     * 注册设备
     */
    @PostMapping("/register")
    @Operation(summary = "注册设备")
    public AjaxResult register(@Valid @RequestBody DeviceRegisterDto dto)
    {
        deviceService.registerDevice(dto);
        return success();
    }


    /**
     * 修改设备
     */
    @PutMapping
    @Operation(summary = "修改设备")
    public AjaxResult update(@Valid @RequestBody DeviceUpdateDto dto)
    {
        deviceService.updateDeviceInfo(dto);
        return success();
    }


    /**
     * 删除设备
     */
    @DeleteMapping("/{iotId}")
    @Operation(summary = "删除设备")
    public AjaxResult delete(@PathVariable("iotId") String iotId)
    {
        deviceService.deleteDeviceInfo(iotId);
        return success();
    }


    /**
     * 查询设备详细信息
     */
    @GetMapping("/{iotId}")
    @Operation(summary = "查询设备详细信息")
    public AjaxResult getDetail(@PathVariable("iotId") String iotId)
    {
        return success(deviceService.getDeviceDetail(iotId));
    }


    /**
     * 查询设备上报的数据
     */
    @GetMapping("/queryServiceProperties/{iotId}")
    @Operation(summary = "查询设备上报的数据")
    public AjaxResult queryServiceProperties(@PathVariable("iotId") String iotId)
    {
        return success(deviceService.queryServiceProperties(iotId));
    }


    @PostMapping("/syncProductList")
    @Operation(summary = "从物联网平台同步产品列表")
    public AjaxResult syncProductList() {
        deviceService.syncProductList();
        return success();
    }


    @GetMapping("/allProduct")
    @Operation(summary = "查询所有产品列表")
    public AjaxResult allProduct() {
        return success(deviceService.allProduct());
    }


    /**
     * 查询产品详情
     */
    @GetMapping("/queryProduct/{productId}")
    @Operation(summary = "查询产品详情")
    public AjaxResult queryProduct(@PathVariable("productId") String productId) {
        return success(deviceService.queryProduct(productId));
    }
}