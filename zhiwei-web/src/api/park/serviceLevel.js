import request from '@/utils/request'

//查询所有服务等级
export function getServiceLevelAll() {
  return request({
    url: '/park/serviceLevel/listAll',
    method: 'get'
  })
}

// 查询服务等级列表
export function listServiceLevel(query) {
  return request({
    url: '/park/serviceLevel/list',
    method: 'get',
    params: query
  })
}

// 查询服务等级详细
export function getServiceLevel(id) {
  return request({
    url: '/park/serviceLevel/' + id,
    method: 'get'
  })
}

// 新增服务等级
export function addServiceLevel(data) {
  return request({
    url: '/park/serviceLevel',
    method: 'post',
    data: data
  })
}

// 修改服务等级
export function updateServiceLevel(data) {
  return request({
    url: '/park/serviceLevel',
    method: 'put',
    data: data
  })
}

// 删除服务等级
export function delServiceLevel(id) {
  return request({
    url: '/park/serviceLevel/' + id,
    method: 'delete'
  })
}
