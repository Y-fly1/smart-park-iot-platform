import request from '@/utils/request'

//查询所有的巡检计划
export function getInspectionPlanAll(query) {
  return request({
    url: '/park/inspectionPlan/all',
    method: 'get',
    params: query
  })
}

// 查询巡检计划列表
export function listInspectionPlan(query) {
  return request({
    url: '/park/inspectionPlan/list',
    method: 'get',
    params: query
  })
}

// 查询巡检计划详细
export function getInspectionPlan(id) {
  return request({
    url: '/park/inspectionPlan/' + id,
    method: 'get'
  })
}

// 新增巡检计划
export function addInspectionPlan(data) {
  return request({
    url: '/park/inspectionPlan',
    method: 'post',
    data: data
  })
}

// 修改巡检计划
export function updateInspectionPlan(data) {
  return request({
    url: '/park/inspectionPlan',
    method: 'put',
    data: data
  })
}

// 修改巡检计划
export function planStatus(data) {
  return request({
    url: '/park/inspectionPlan',
    method: 'put',
    data: data
  })
}

// 删除巡检计划
export function delInspectionPlan(id) {
  return request({
    url: '/park/inspectionPlan/' + id,
    method: 'delete'
  })
}
