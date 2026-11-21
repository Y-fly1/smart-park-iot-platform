### 修改设备

**接口地址**:`/park/device`

**请求方式**:`PUT`

**请求示例**:

```JSON
{
  "id":"12",//设备主键id
  "deviceName":"手表1号",//设备名称
  "bindingLocation": "540",//由于绑定的是企业，这个值是企业id
  "locationType": 0,//位置类型 0：随身设备 1：固定设备
  "nodeId": "12345",//设备节点id
  "physicalLocationType": -1,//如果是随身设备，传-1
  "productKey": "6625d23371d845632a067bdf",//产品id
  "productName": "watch",//产品名称
  "remark": "张小丽", //由于绑定的是企业，这个值是企业的姓名
  "deviceDescription": "1"//绑定的是企业，这个值是企业id  ,方便数据回显
}
```

**响应示例**:

```JavaScript
{"msg":"操作成功","code":200}
```



# 华为云官方接口文档

### 修改设备

https://support.huaweicloud.com/api-iothub/iot_06_v5_1079.html





