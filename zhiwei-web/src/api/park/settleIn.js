import request from '@/utils/request'

//查询企业列表
export function listTenantPageQuery (query) {
  return request({
    url: '/park/tenant/list',
    method: 'get',
    params: query
  })
}
//查询企业列表
export function healthPageQuery (query) {
  return request({
    url: '/park/energyAssessment/list',
    method: 'get',
    params: query
  })
}
//申请入驻
export function applySettleIn (data) {
  return request({
    url: '/park/settleIn/apply',
    method: 'post',
    data: data
  })
}

//入驻详情
export function getSettleInInfo (id) {
  return request({
    url: '/park/settleIn/detail/'+id,
    method: 'get'
  })
}

// 查询入驻列表
export function listSettleIn(query) {
  return request({
    url: '/park/settleIn/list',
    method: 'get',
    params: query
  })
}

// 查询入驻详细
export function getSettleIn(id) {
  return request({
    url: '/park/settleIn/' + id,
    method: 'get'
  })
}

// 新增入驻
export function addSettleIn(data) {
  return request({
    url: '/park/settleIn',
    method: 'post',
    data: data
  })
}

// 修改入驻
export function updateSettleIn(data) {
  return request({
    url: '/park/settleIn',
    method: 'put',
    data: data
  })
}

// 删除入驻
export function delSettleIn(id) {
  return request({
    url: '/park/settleIn/' + id,
    method: 'delete'
  })
}
// 查询企业信息详情
export function getOldSettleIn(id) {
  return request({
    url: '/park/energyAssessment/tenant/' + id,
    method: 'get'
  })
}