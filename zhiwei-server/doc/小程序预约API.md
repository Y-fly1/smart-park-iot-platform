2.1 查询取消预约数量
- 接口地址:/member/reservation/cancelled-count
- 请求方式:GET
- 请求参数:
  无，我们需要获取线程中的用户即可，在后端获取，不需要前端传任何参数
- 响应示例
  {
  "code": 200,
  "msg": "操作成功",
  "data": 0  //取消预约次数，当天取消预约次数大于3次，则不让预约
  }


- 
  2.2 查询每个时间段剩余预约次数
- 接口地址:/member/reservation/countByTime
- 请求方式:GET
- 请求参数:
  参数名称	参数说明	数据类型
  time	时间	long(时间戳)
- 返回示例：
  {
  "code": 200,
  "msg": "操作成功",
  "data": [
  {
  "time": "2024-09-19 08:00:00",
  "count": 5  //目前剩余5次
  },
  {
  "time": "2024-09-19 10:30:00",
  "count": 0    //当前时间段已约满，不能再次选择该时间，这个时间返回的是剩余预约次数
  }
  //其他时间段，没有返回，表示可以预约6次
  ]
  }


2.3 新增预约
- 接口地址:/member/reservation/
- 请求方式:POST
- 请求参数:
  {
  "mobile": "13211223569",//手机号
  "name": "李天天",//预约人
  "time": "2024-09-19 10:30:00",//时间
  "type": 0,//预约类型  0为参观预约
  "visitor": "李霸天"//家人姓名（企业名称）
  }
- 响应示例：
  {
  "code": 200,
  "msg": "操作成功"
  }


  
2.4 分页查询预约
- 接口地址:/member/reservation/page
- 请求方式:GET
- 请求参数:
  参数名称	参数说明	数据类型
  pageNum	页码	int
  pageSize	每页数量	int
  status	状态	int
- 响应示例：
  {
  "code": 200,
  "msg": "操作成功",
  "data": {
  "total": 9,
  "rows": [
  {
  "createBy": "3",
  "createTime": "2024-09-19 00:45:00",
  "updateBy": "3",
  "updateTime": null,
  "remark": null,
  "id": 110,
  "name": "李天天",
  "mobile": "13211223543",
  "time": "2024-09-19T10:30:00",
  "visitor": "李霸天",
  "type": 0,
  "status": 0
  }
  ],
  "code": 200,
  "msg": "请求成功"
  }
  }



2.5 取消预约
- 接口地址:/member/reservation/{id}/cancel
- 请求方式:PUT
- 请求参数:
  路径参数
  参数名称	参数说明	数据类型
  id	主键	long
- 响应示例：
  {
  "code": 200,
  "msg": "操作成功"
  }