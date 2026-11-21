<template>
  <div ref="mainHeight" class="bg-wt">
    <!-- <el-card header="疾病诊断">
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-160">淘汰设备：</h1>
          <span>{{ detailData.diseaseDiagnosis.obsoleteEquipment }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">私拉乱接：</h1>
          <span>{{ detailData.diseaseDiagnosis.illegalWiring }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-160">超容用电：</h1>
          <span>{{ detailData.diseaseDiagnosis.overCapacity }}</span>
        </div>
      </div>
    </el-card> -->
  </div>
</template>
<script setup>
import { reactive, watch, ref, computed, onMounted } from 'vue';
// 组件
// 不同年龄人群能效指数分布
import AgeDistribution from './AgeDistribution.vue';
// 不同系统能效指数分布
import SystemDistribution from './SystemDistribution.vue';
const props = defineProps({
  baseData: {
    type: Object,
    default: () => {},
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
const detailData = ref({
  diseaseDiagnosis: {},
  recent30Days: {},
});
watch(() => props.baseData, (val) => { 
  detailData.value=val.energyAssessment
  console.log(detailData.value)
},
  { deep: true});
</script>