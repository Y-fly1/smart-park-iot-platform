import request from '@/utils/request'

// 查询能效评估列表
export function listEnergyAssessment(query) {
  return request({
    url: '/park/energyAssessment/list',
    method: 'get',
    params: query
  })
}

// 查询能效评估详细
export function getEnergyAssessment(id) {
  return request({
    url: '/park/energyAssessment/' + id,
    method: 'get'
  })
}
// 获取能效评估记录详细信息
export function getReportAssessment(id) {
  return request({
    url: '/park/energyAssessment/report/' + id,
    method: 'get'
  })
}
// 新增能效评估
export function addEnergyAssessment(data) {
  return request({
    url: '/park/energyAssessment',
    method: 'post',
    data: data
  })
}
// 评估数据
export function assessmentData(data) {
  return request({
    url: '/park/energyAssessment/assessmentData',
    method: 'post',
    data: data
  })
}
// 修改能效评估
export function updateEnergyAssessment(data) {
  return request({
    url: '/park/energyAssessment',
    method: 'put',
    data: data
  })
}

// 删除能效评估
export function delEnergyAssessment(id) {
  return request({
    url: '/park/energyAssessment/' + id,
    method: 'delete'
  })
}
// 取消能效评估
export function concelEnergyAssessment(id) {
  return request({
    url: '/park/energyAssessment/' + id,
    method: 'put'
  })
}