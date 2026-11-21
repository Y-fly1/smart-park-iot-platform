import request from '@/utils/request'

// 查询报警数据列表
export function listAlertData(query) {
  return request({
    url: '/park/alertData/list',
    method: 'get',
    params: query
  })
}
// 处理报警
export function handleAlertRule(data) {
  return request({
    url: '/park/alertData/handleAlertData',
    method: 'put',
    data: data
  })
}
// 查询报警数据详细
export function getAlertData(id) {
  return request({
    url: '/park/alertData/' + id,
    method: 'get'
  })
}

// 新增报警数据
export function addAlertData(data) {
  return request({
    url: '/park/alertData',
    method: 'post',
    data: data
  })
}

// 修改报警数据
export function updateAlertData(data) {
  return request({
    url: '/park/alertData',
    method: 'put',
    data: data
  })
}

// 删除报警数据
export function delAlertData(id) {
  return request({
    url: '/park/alertData/' + id,
    method: 'delete'
  })
}
