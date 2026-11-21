<template>
  <div ref="mainHeight" class="bg-wt">
    <!-- 使用可选链操作符安全访问 -->
    <el-card header="疾病诊断" v-if="detailData?.diseaseDiagnosis">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-110">淘汰设备：</h1>
          <span>{{ detailData.diseaseDiagnosis.obsoleteEquipment || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">私拉乱接：</h1>
          <span>{{ detailData.diseaseDiagnosis.illegalWiring || '--' }}</span>
        </div>
        <div class="info-item wlong">
          <h1 class="label-wt-110">超容用电：</h1>
          <span>{{ detailData.diseaseDiagnosis.overCapacity || '--' }}</span>
        </div>
      </div>
    </el-card>

    <el-card header="近30天内意外事件" v-if="detailData?.recent30Days">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-110">超限告警：</h1>
          <span>{{ getEventText(detailData.recent30Days.overLimit) }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">夜间异常：</h1>
          <span>{{ getEventText(detailData.recent30Days.nightAbnormal) }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">设备故障：</h1>
          <span>{{ getEventText(detailData.recent30Days.equipmentFailure) }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">违规用能：</h1>
          <span>{{ getEventText(detailData.recent30Days.violation) }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-110">停电停水：</h1>
          <span>{{ getEventText(detailData.recent30Days.outage) }}</span>
        </div>
      </div>
    </el-card>

    <el-card header="近30天能耗账单" v-if="detailData?.recent30Days?.energyBill">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-110">能耗账单：</h1>
          <span>
            <i>
              {{tenantName}}的能耗账单.{{getFileType(detailData.recent30Days.energyBill)}}
              <a class="font-bt" :href="detailData.recent30Days.energyBill" target="_blank">查看</a>
            </i>
          </span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, watch, ref, computed, onMounted } from 'vue';
import {getFileType} from '@/utils';
const props = defineProps({
  baseData: {
    type: Object,
    default: () => ({}),
  },
  systemScore: {
    type: Array,
    default: () => [],
  },
  abnormalAnalysis: {
    type: Array,
    default: () => [],
  },
});
const tenantName = ref('')
const ptionsData = [
  { label: '无', value: 0 },
  { label: '发生过1次', value: 1 },
  { label: '发生过2次', value: 2 },
  { label: '发生过3次以上', value: 3 },
];

// 初始化 detailData 为完整的结构，确保所有属性都有默认值
const detailData = ref({
  diseaseDiagnosis: {
    obsoleteEquipment: '',
    illegalWiring: '',
    overCapacity: ''
  },
  recent30Days: {
    overLimit: '',
    nightAbnormal: '',
    equipmentFailure: '',
    violation: '',
    outage: '',
    energyBill: ''
  }
});

// 将数字映射为文本
const getEventText = (value) => {
  if (value === undefined || value === null || value === '') {
    return '--';
  }
  const option = ptionsData.find(item => item.value === value);
  return option ? option.label : '--';
};

// 安全处理 props 数据
watch(() => props.baseData, (val) => { 
  console.log('baseData 变化:', val);
  
  if (val?.energyAssessment) {
    // 合并数据，确保结构完整
    tenantName.value = val.basicInfo.tenantName
    detailData.value = {
      diseaseDiagnosis: {
        ...detailData.value.diseaseDiagnosis,
        ...(val.energyAssessment.diseaseDiagnosis || {})
      },
      recent30Days: {
        ...detailData.value.recent30Days,
        ...(val.energyAssessment.recent30Days || {})
      }
    };
  } else {
    // 如果没有数据，重置为默认值
    detailData.value = {
      diseaseDiagnosis: {
        obsoleteEquipment: '',
        illegalWiring: '',
        overCapacity: ''
      },
      recent30Days: {
        overLimit: '',
        nightAbnormal: '',
        equipmentFailure: '',
        violation: '',
        outage: '',
        energyBill: ''
      }
    };
  }
  
  console.log('detailData 更新后:', detailData.value);
}, { deep: true, immediate: true });

// 或者使用 computed 属性，更简洁
// const detailData = computed(() => {
//   const healthData = props.baseData?.energyAssessment || {};
//   return {
//     diseaseDiagnosis: {
//       obsoleteEquipment: '',
//       illegalWiring: '',
//       overCapacity: '',
//       ...healthData.diseaseDiagnosis
//     },
//     recent30Days: {
//       overLimit: '',
//       nightAbnormal: '',
//       equipmentFailure: '',
//       violation: '',
//       outage: '',
//       energyBill: '',
//       ...healthData.recent30Days
//     }
//   };
// });
</script>

<style scoped>
/* 确保链接正常显示 */

.startTop {
  margin-top: 10px;
}
</style>