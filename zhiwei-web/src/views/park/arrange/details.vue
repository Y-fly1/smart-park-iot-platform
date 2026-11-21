<template>
  <div class="detail-base intelligenc">
    <!-- 基本信息 -->
    <div ref="mainHeight" class="bg-wt">
      <el-card header="基本信息">
        <div class="info-block">
          <div class="info-item">
            <h1 class="label-wt-long">企业名称：</h1>
            <span>{{ baseData.tenantName || '--' }}</span>
          </div>
          <div class="info-item">
            <h1 class="label-wt-long">服务等级：</h1>
            <span>{{ baseData.serviceLevelName || '--' }}</span>
          </div>
          <div class="info-item">
            <h1 class="label-wt-long">性别：</h1>
            <span>{{ baseData.sex || '--' }}</span>
          </div>
          <div class="info-item">
            <h1 class="label-wt-long">年龄：</h1>
            <span>{{ baseData.age || '--' }}</span>
          </div>
          <div class="info-item">
            <h1 class="label-wt-long">点位编号：</h1>
            <span>{{ baseData.pointCode }}</span>
          </div>
          <div class="info-item">
            <h1 class="label-wt-long">巡检员姓名：</h1>
            <span>{{
              baseData.inspectionItemName ? baseData.inspectionItemName.join(',') : '--'
            }}</span>
          </div>
        </div>
      </el-card>
      <!-- 巡检项 -->
      <el-card
        header="巡检项"
        :class="baseData.status === 1 ? 'noBottomLine' : ''"
      >
        <div class="info-block">
          <div class="info-item">
            <h1 class="label-wt-long">巡检项名称：</h1>
            <span>{{ baseData.projectName }}</span>
          </div>
          <div class="info-item">
            <h1 class="label-wt-long">执行状态：</h1>
            <span>{{
              baseData.status === 1
                ? '待执行'
                : baseData.status === 2
                ? '已执行'
                : '已取消'
            }}</span>
          </div>
          <div class="info-item">
            <h1 class="label-wt-long">创建时间：</h1>
            <span>{{ baseData.createTime }}</span>
          </div>
          <div class="info-item">
            <h1 class="label-wt-long">期望服务时间：</h1>
            <span>{{ baseData.estimatedServerTime }}</span>
          </div>
          <div class="info-item">
            <h1 class="label-wt-long">备注信息：</h1>
            <span>{{ baseData.remark || '--' }}</span>
          </div>
        </div>
      </el-card>
      <!-- end -->
      <!-- 执行记录 -->
      <div v-if="baseData.status === 2">
        <el-card header="执行记录">
          <div class="info-block">
            <div class="info-item">
              <h1 class="label-wt-long">执行人：</h1>
              <span>{{ baseData.updater }}</span>
            </div>
            <div class="info-item">
              <h1 class="label-wt-long">执行时间：</h1>
              <span>{{ baseData.realServerTime || '--' }}</span>
            </div>
            <div class="info-item">
              <h1 class="label-wt-long">执行图片：</h1>
              <span class="img">
                <img
                    alt="test"
                    :src="baseData.taskImage"
                    class="tdesign-demo-image-viewer__ui-image--img"
                  />
              </span>
            </div>
            <div class="info-item">
              <h1 class="label-wt-long">执行记录：</h1>
              <span>{{ baseData.mark }}</span>
            </div>
          </div>
        </el-card>
      </div>
      <!-- end -->
      <!-- 取消记录 -->
      <div v-if="baseData.status === 3">
          <el-card header="取消记录">
          <div class="info-block">
            <div class="info-item">
              <h1 class="label-wt-long">取消人：</h1>
              <span>{{ baseData.updater }}</span>
            </div>
            <div class="info-item">
              <h1 class="label-wt-long">取消时间：</h1>
              <span>{{ baseData.updateTime }}</span>
            </div>
            <div class="info-item">
              <h1 class="label-wt-long">取消原因：</h1>
              <span>{{ baseData.cancelReason }}</span>
            </div>
          </div>
        </el-card>
        <!-- end -->
      </div>
    </div>
    <!-- end -->
  </div>
</template>
<script setup>
// 接口
import {getInspectionTask,} from '@/api/park/inspectionTask';

// ------定义变量------
const route = useRoute(); // 获取局部
const router = useRouter(); // 获取全局
const baseData = ref({});
const taskId = ref(''); // 账单id
// 生命周期
onMounted(() => {
  taskId.value = route.query.id;
  getBaseData()
});
// ------定义方法------
// // 获取列表数据
const getBaseData = async () => {
  const res = await getInspectionTask(taskId.value); // 获取列表数据
  if (res.code === 200) {
    baseData.value = res.data;
    // 年龄需要前端算
    baseData.value.age = getBirthday(baseData.value.creditCode).age;
  }
};
// 返回
const handleReturn = () => {
  router.go(-1);
};
</script>
