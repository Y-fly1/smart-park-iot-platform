import request from '@/utils/request'

// 查询报警规则列表
export function listAlertRule(query) {
  return request({
    url: '/park/alertRule/list',
    method: 'get',
    params: query
  })
}

// 查询报警规则详细
export function getAlertRule(id) {
  return request({
    url: '/park/alertRule/' + id,
    method: 'get'
  })
}

// 新增报警规则
export function addAlertRule(data) {
  return request({
    url: '/park/alertRule',
    method: 'post',
    data: data
  })
}

// 修改报警规则
export function updateAlertRule(data) {
  return request({
    url: '/park/alertRule',
    method: 'put',
    data: data
  })
}

// 删除报警规则
export function delAlertRule(id) {
  return request({
    url: '/park/alertRule/' + id,
    method: 'delete'
  })
}
