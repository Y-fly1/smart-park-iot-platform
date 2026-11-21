import request from '@/utils/request';

// 查询巡检任务列表
export function listInspectionTask(query) {
  return request({
    url: '/park/inspectionTask/list',
    method: 'get',
    params: query,
  });
}


// 查询巡检任务详细信息
export function getInspectionTask(id) {
  return request({
    url: '/park/inspectionTask/'+id,
    method: 'get'
  });
}

// 取消巡检任务
export function cancelPlan(params) {
  return request({
    url: `/park/inspectionTask/cancel`,
    method: 'put',
    data:params
  });
}

// 执行任务
export function executePlan(params) {
  return request({
    url: `/park/inspectionTask/do`,
    method: 'put',
    data:params
  });
}

// 修改巡检任务的执行日期
export function changePlanTime(params) {
  return request({
    url: `/park/inspectionTask/updateTime`,
    method: 'put',
    data:params
  });
}