import request from '@/utils/request'

// 查询巡检项列表
export function listProject(query) {
  return request({
    url: '/park/inspectionItem/list',
    method: 'get',
    params: query
  })
}

// 查询所有的服务等级
export function getProjectAll() {
  return request({
    url: '/park/inspectionItem/all',
    method: 'get'
  })
}

// 查询巡检项详细
export function getProject(id) {
  return request({
    url: '/park/inspectionItem/' + id,
    method: 'get'
  })
}

// 新增巡检项
export function addProject(data) {
  return request({
    url: '/park/inspectionItem',
    method: 'post',
    data: data
  })
}

// 修改巡检项
export function updateProject(data) {
  return request({
    url: '/park/inspectionItem',
    method: 'put',
    data: data
  })
}

// 删除巡检项
export function delProject(id) {
  return request({
    url: '/park/inspectionItem/' + id,
    method: 'delete'
  })
}
