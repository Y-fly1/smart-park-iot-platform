<!-- 需要展示的基本信息 -->
<template>
  <div>
    <!-- 基本信息 -->
    <el-card>
      <h3>
        <span class="header-title">基本信息</span>
      </h3>
      <div class="info-block">
        <div class="info-item">
          <h1 class="label-wt-120">企业名称：</h1>
          <span>{{ baseData.name }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">统一社会信用代码：</h1>
          <span>{{ baseData.creditCode }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">出生日期：</h1>
          <span>{{ baseData.birthday }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">年龄：</h1>
          <span>{{ baseData.age }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">性别：</h1>
          <span>{{ baseData.sex === 0 ? '男' : '女' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">法人代表号：</h1>
          <span>{{ baseData.socialSecurityCard || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">所属片区：</h1>
          <span>
            {{ baseData.nation }}
            <span
              v-if="baseData.nation === '少数所属片区' && baseData.nationDetail"
            >
              （{{ baseData.nationDetail }}）
            </span>
          </span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">文化程度：</h1>
          <span>{{ baseData.educationLevel || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">主要用能：</h1>
          <span>
            {{ baseData.religiousBelief }}
            <span
              v-if="
                baseData.religiousBelief === '有' &&
                baseData.religiousBeliefDetail
              "
            >
              （{{ baseData.religiousBeliefDetail }}）
            </span>
          </span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">企业类型：</h1>
          <span>{{ baseData.maritalStatus || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">经营状态：</h1>
          <span>{{ baseData.livingSituation || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">医疗费用支付方式：</h1>
          <span>{{ baseData.medicalPaymentMethod || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">用能结构：</h1>
          <span>{{ baseData.economicSource || '--' }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">企业联系方式：</h1>
          <span>{{ baseData.phone || '--' }}</span>
        </div>

        <div class="info-item">
          <h1 class="label-wt-120">核心建议：</h1>
          <span>
            <!-- <span v-if="baseData.coreSuggestion === 0" type="danger"
              >不建议入驻</span
            >-->
            <span v-if="baseData.coreSuggestion" :class="baseData.coreSuggestion==='建议入驻'?'tagCol1':'tagCol2'"
              >{{baseData.coreSuggestion}}</span
            > 
            <span v-else>--</span>
          </span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">家庭住址：</h1>
          <span>{{ baseData.address }}</span>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">一寸照片：</h1>
          <ImagePreview :src="baseData.image" :width="'100px'" :height="'100px'"></ImagePreview>
        </div>
        <div class="info-item">
          <h1 class="label-wt-130">信用代码人像面：</h1>
          <ImagePreview :src="baseData.licenseImg" :width="'100px'" :height="'100px'"></ImagePreview>
        </div>
        <div class="info-item">
          <h1 class="label-wt-120">信用代码国徽面：</h1>
          <ImagePreview :src="baseData.licenseCopyImg" :width="'100px'" :height="'100px'"></ImagePreview>
        </div>
      </div>
    </el-card>
    <!-- end -->
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
// 获取父组件值、方法
const props = defineProps({
  // 基本信息数据
  baseData: {
    type: Object,
    default: () => {
      return {};
    },
  },
});
// ------定义变量------
const oneInchPhoto = ref([
  {
    url: '',
  },
]);
const licenseCopyImg = ref([
  {
    url: '',
  },
]);
const licenseImg = ref([
  {
    url: '',
  },
]);
// 绑定上传的文件
watch(props, () => {
  const data = props.baseData;
  oneInchPhoto.value[0].url = data.oneInchPhoto;
  licenseCopyImg.value[0].url = data.licenseCopyImg;
  licenseImg.value[0].url = data.licenseImg;
});
</script>
