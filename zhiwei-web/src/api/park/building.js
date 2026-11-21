import request from '@/utils/request'

export function getBuildingPoint(status) {
  return request({
    url: '/tenant/building/getRoomAndPointByPointStatus/'+status,
    method: 'get'
  })
}

export function getRootById(id) {
  return request({
    url: '/tenant/room/one/'+id,
    method: 'get'
  })
}

// 查询楼栋列表
export function listBuilding(query) {
  return request({
    url: '/tenant/building/list',
    method: 'get',
    params: query
  })
}

// 查询楼栋详细
export function getBuildingDetails(id) {
  return request({
    url: '/tenant/building/' + id,
    method: 'get'
  })
}

// 新增楼栋
export function addBuilding(data) {
  return request({
    url: '/tenant/building',
    method: 'post',
    data: data
  })
}

// 修改楼栋
export function updateBuilding(data) {
  return request({
    url: '/tenant/building',
    method: 'put',
    data: data
  })
}

// 删除楼栋
export function delBuilding(id) {
  return request({
    url: '/tenant/building/' + id,
    method: 'delete'
  })
}
// 获取房间数据
export function getRoomPointList(id) {
  return request({
    url: '/tenant/room/getRoomsByBuildingId/'+id,
    method: 'get'
  })
}
// 查询房间详细
export function getRoomDetails(id) {
  return request({
    url: '/tenant/room/' + id,
    method: 'get'
  })
}

// 新增房间
export function addRoom(data) {
  return request({
    url: '/tenant/room',
    method: 'post',
    data: data
  })
}

// 修改房间
export function updateRoom(data) {
  return request({
    url: '/tenant/room',
    method: 'put',
    data: data
  })
}
// 删除房间
export function delRoom(id) {
  return request({
    url: '/tenant/room/' + id,
    method: 'delete'
  })
}
// 查询房间详细
export function getPointDetails(id) {
  return request({
    url: '/tenant/point/' + id,
    method: 'get'
  })
}
// 新增点位
export function addPoint(data) {
  return request({
    url: '/tenant/point',
    method: 'post',
    data: data
  })
}

// 修改点位
export function updatePoint(data) {
  return request({
    url: '/tenant/point',
    method: 'put',
    data: data
  })
}
// 删除点位
export function delPoint(id) {
  return request({
    url: '/tenant/point/' + id,
    method: 'delete'
  })
}

// 智能点位
// 查询所有楼栋（智能设备）
export function listAllDeviceBuilding(query) {
  return request({
    url: '/tenant/building/getAllBuildingsWithDevice',
    method: 'get',
    params: query
  })
}
// 获取房间数据（智能点位）
export function getRoomPointDeviceList(id) {
  return request({
    url: '/tenant/room/getRoomsWithDeviceByBuildingId/'+id,
    method: 'get'
  })
}
// 巡检任务
// 查询所有楼栋（负责企业）
export function listAllWithNurBuilding(query) {
  return request({
    url: '/tenant/building/getAllBuildingsWithNur',
    method: 'get',
    params: query
  })
}
// 获取房间数据（负责企业）
export function getRoomPointWithNurList(id) {
  return request({
    url: '/tenant/room/getRoomsWithNurByBuildingId/'+id,
    method: 'get'
  })
}
// 设置巡检员
export function setInspection(data) {
  return request({
    url: '/tenant/inspectionTenant/setInspection',
    method: 'post',
    data: data
  })
}