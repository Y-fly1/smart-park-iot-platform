import request from '@/utils/request'

// 查询区域类型列表
export function listAreaType(query) {
  return request({
    url: '/tenant/areaType/list',
    method: 'get',
    params: query
  })
}
// 获取所有区域类型列表
export function getAreaTypesList() {
  return request({
    url: '/tenant/areaType/listAll',
    method: 'get'
  })
}
// 查询区域类型详细
export function getAreaType(id) {
  return request({
    url: '/tenant/areaType/' + id,
    method: 'get'
  })
}

// 新增区域类型
export function addAreaType(data) {
  return request({
    url: '/tenant/areaType',
    method: 'post',
    data: data
  })
}

// 修改区域类型
export function updateAreaType(data) {
  return request({
    url: '/tenant/areaType',
    method: 'put',
    data: data
  })
}

// 删除区域类型
export function delAreaType(id) {
  return request({
    url: '/tenant/areaType/' + id,
    method: 'delete'
  })
}
