<template>
  <div class="info">
    <h3>
      入驻配置<el-tooltip
        class="item"
        effect="dark"
        content="该费用为月账单，每月1号收当月费用"
        placement="top"
      >
        <el-icon><WarningFilled /></el-icon>
      </el-tooltip>
    </h3>
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="120px">
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="入驻期限" prop="settleInTime">
            <el-date-picker
              type="datetimerange"
              :readonly="type === 'read'"
              v-model="settleInTime"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              start-placeholder="请选择开始日期"
              end-placeholder="请选择结束日期"
              range-separator="-"
              clearable
              @change="hendleSettleInTime"
              :disabled-date="disabledDate"
              class="datePicker"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="费用期限" prop="feeTime">
            <el-date-picker
              type="datetimerange"
              :readonly="type === 'read'"
              v-model="feeTime"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              start-placeholder="请选择开始日期"
              end-placeholder="请选择结束日期"
              range-separator="-"
              clearable
              @change="hendleFeeTime"
              :disabled-date="disabledDate"
              class="datePicker"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="服务等级" prop="serviceLevelId">
            <el-select
              v-model="formData.serviceLevelId"
              placeholder="请选择"
              clearable
              @change="handleInspection"
            >
              <el-option
                v-for="(item, index) in serviceLevelIdOptions"
                :key="index"
                :label="item.name"
                :value="item.id"
                :disabled="item.disabled"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="入驻点位" prop="point">
            <div v-if="type === 'read'">
              {{ formData.pointCode }}
            </div>
            <el-cascader
              v-else
              v-model="formData.point"
              :options="pointIdOptions"
              :props="bedProps"
              @change="handleBuilding"
              placeholder="请选择"
              clearable
            ></el-cascader>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="押金">
            <span class="method">+</span>
            <el-input
              v-model="formData.deposit"
              :disabled="type === 'read'"
              type="text"
              placeholder="请输入"
              clearable
              :style="{ width: '150px' }"
            ></el-input><span class="unit">元</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务费用" prop="serviceFee">
            <span class="method">+</span>
            <el-input-number
              v-model="formData.serviceFee"
              placeholder="0.00"
              :min="0"
              :step="10"
              large-number
              :max="99999999"
              @blur="textBlurPrice($event, 1)"
              @change="textBlurPrice($event, 1)"
            ></el-input-number><span class="unit">元/月</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="场地费用" prop="siteFee">
            <span class="method">+</span>
            <el-input-number
              v-model="formData.siteFee"
              placeholder="0.00"
              :min="0"
              :step="10"
              large-number
              :max="99999999"
              :decimal-places="2"
              @blur="textBlurPrice($event, 2)"
              @change="textBlurPrice($event, 2)"
            ></el-input-number><span class="unit">元/月</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他费用">
            <span class="method">+</span>
            <el-input-number
              v-model="formData.otherFees"
              :min="0"
              :step="10"
              large-number
              :max="99999999"
              placeholder="0.00"
              :decimal-places="2"
              @blur="textBlurPrice($event, 3)"
              @change="textBlurPrice($event, 3)"
            ></el-input-number><span class="unit">元/月</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row gutter="15">
        <el-col :span="12">
          <el-form-item label="医保费用">
            <span class="method2">- </span>
            <el-input-number
              v-model="formData.insurancePayment"
              :min="0"
              :step="10"
              large-number
              :max="99999999"
              placeholder="0.00"
              :decimal-places="2"
              @blur="textBlurPrice($event, 4)"
              @change="textBlurPrice($event, 4)"
            ></el-input-number><span class="unit">元/月</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="政府补贴">
            <span class="method2"> - </span>
            <el-input-number
              v-model="formData.governmentSubsidy"
              :min="0"
              :step="10"
              large-number
              :max="99999999"
              placeholder="0.00"
              :decimal-places="2"
              @blur="textBlurPrice($event, 5)"
              @change="textBlurPrice($event, 5)"
            ></el-input-number><span class="unit">元/月</span>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script setup>
import { getServiceLevelAll } from '@/api/park/serviceLevel';
import { getBuildingPoint, getRootById } from '@/api/park/building';
import { onMounted, nextTick } from 'vue';

const formRef = ref();

// 存储原始费用值用于校验
const originalInspectionFee = ref(null);
const originalPointFee = ref(null);

// 定义校验规则
const rules = ref({
  settleInTime: [
    {
      required: true,
      message: '入驻期限为空，请选择入驻期限',
      trigger: 'change',
    },
  ],
  feeTime: [
    {
      required: true,
      message: '费用期限为空，请选择费用期限',
      trigger: 'change',
    },
  ],
  serviceLevelId: [
    {
      required: true,
      message: '服务等级为空，请选择服务等级',
      trigger: 'change',
    },
  ],
  point: [
    {
      required: true,
      type: 'array',
      message: '入驻点位为空，请选择入驻点位',
      trigger: 'change',
    },
  ],
  serviceFee: [
    {
      required: true,
      message: '服务费用为空，请输入服务费用',
      trigger: 'blur',
    },
    {
      validator: (rule, val, callback) => {
        if (val === undefined || val === null || val === '') {
          callback(new Error('服务费用为空，请输入服务费用'));
        } else if (val < 0.01) {
          callback(new Error('服务费用必须大于0'));
        } else if (originalInspectionFee.value && (val < originalInspectionFee.value * 0.9 || val > originalInspectionFee.value * 1.1)) {
          callback(new Error(`该费用已超出/低于原费用10%，请重新输入`));
        } else {
          callback();
        }
      },
      trigger: ['blur', 'change']
    }
  ],
  siteFee: [
    {
      required: true,
      message: '场地费用为空，请输入场地费用',
      trigger: 'blur',
    },
    {
      validator: (rule, val, callback) => {
        if (val === undefined || val === null || val === '') {
          callback(new Error('场地费用为空，请输入场地费用'));
        } else if (val < 0.01) {
          callback(new Error('场地费用必须大于0'));
        } else if (originalPointFee.value && (val < originalPointFee.value * 0.9 || val > originalPointFee.value * 1.1)) {
          callback(new Error(`该费用已超出/低于原费用10%，请重新输入`));
        } else {
          callback();
        }
      },
      trigger: ['blur', 'change']
    }
  ],
});

const data = reactive({
  formData: {
    settleInTime: '',
    feeTime: '',
    serviceLevelId: undefined,
    point: [],
    deposit: 3000,
    serviceFee: undefined,
    siteFee: undefined,
    otherFees: undefined,
    insurancePayment: undefined,
    governmentSubsidy: undefined,
    startDate: undefined,
    endDate: undefined,
    feeStartDate: undefined,
    feeEndDate: undefined,
    pointId: undefined,
    floorName: undefined,
    buildingId: undefined,
    code: undefined,
    roomId: undefined,
    serviceLevelName: undefined,
    pointCode: undefined,
    // 存储原始费用
    originalInspectionFee: undefined,
    originalPointFee: undefined,
  },
});
const { formData } = toRefs(data);

const serviceLevelIdOptions = ref([]);
const pointIdOptions = ref([]);

// 禁用日期函数：早于当前日期的日期置灰
const disabledDate = (time) => {
  // 获取当前日期的开始时间（00:00:00）
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  // 如果选择的时间小于今天，则禁用
  return time.getTime() < today.getTime();
};

// 单独监听服务费用变化，触发服务费用校验
watch(
  () => formData.value.serviceFee,
  () => {
    nextTick(() => {
      if (formRef.value) {
        formRef.value.validateField('serviceFee');
      }
    });
  },
);

// 单独监听场地费用变化，触发场地费用校验
watch(
  () => formData.value.siteFee,
  () => {
    nextTick(() => {
      if (formRef.value) {
        formRef.value.validateField('siteFee');
      }
    });
  },
);

onMounted(() => {
  //初始化服务等级
  getServiceLevelAll().then((res) => {
    serviceLevelIdOptions.value = res.data;
  });

  //初始化楼栋房间点位
  getBuildingPoint(0).then((res) => {
    if (res.code === 200) {
      pointIdOptions.value = res.data;
    }
  });
});

//选择点位之后，给属性赋值
//val===>['6','39','58']  第一个值为楼栋id, 第二个为房间id,第三个为点位id
const handleBuilding = (val) => {
  console.log(val);
  formData.value.pointId = val[2];
  formData.value.point = val;
  getRootById(val[1]).then((res) => {
    if (res.code === 200) {
      formData.value.floorName = res.data.floorName;
      formData.value.buildingId = res.data.buildingId;
      formData.value.siteFee = res.data.price;
      // 设置原始场地费用
      originalPointFee.value = res.data.price;
      formData.value.originalPointFee = res.data.price;
      formData.value.code = res.data.code;
      formData.value.roomId = res.data.roomId;
      formData.value.pointCode = res.data.code;
      
      // 只触发场地费用的校验，不影响服务费用
      nextTick(() => {
        if (formRef.value) {
          formRef.value.validateField('siteFee');
        }
      });
    }
  });
};

//处理入驻时间
const hendleSettleInTime = (val) => {
  if (val && val.length > 0) {
    formData.value.startDate = val[0];
    formData.value.endDate = val[1];
    formData.value.settleInTime = val;
  } else {
    formData.value.startDate = undefined;
    formData.value.endDate = undefined;
    formData.value.settleInTime = undefined;
  }

  // 触发校验
  nextTick(() => {
    if (formRef.value) {
      formRef.value.validateField('settleInTime');
    }
  });
};

//处理费用时间
const hendleFeeTime = (val) => {
  if (val && val.length > 0) {
    formData.value.feeStartDate = val[0];
    formData.value.feeEndDate = val[1];
    formData.value.feeTime = val;
  } else {
    formData.value.feeStartDate = undefined;
    formData.value.feeEndDate = undefined;
    formData.value.feeTime = undefined;
  }

  // 触发校验
  nextTick(() => {
    if (formRef.value) {
      formRef.value.validateField('feeTime');
    }
  });
};

//选择了服务等级之后，触发，给服务费用和巡检名称赋值
const handleInspection = () => {
  if (formData.value.serviceLevelId) {
    //根据id从集合中找对应的服务等级对象
    const obj = serviceLevelIdOptions.value.find(
      (item) => item.id === formData.value.serviceLevelId,
    );
    console.log(obj);
    //给属性赋值
    formData.value.serviceFee = obj.fee;
    // 设置原始服务费用
    originalInspectionFee.value = obj.fee;
    formData.value.originalInspectionFee = obj.fee;
    formData.value.serviceLevelName = obj.name;
    
    // 只触发服务费用的校验，不影响场地费用
    nextTick(() => {
      if (formRef.value) {
        formRef.value.validateField('serviceFee');
        formRef.value.validateField('serviceLevelId');
      }
    });
  } else {
    delete formData.value.serviceFee;
    delete formData.value.serviceLevelName;
    originalInspectionFee.value = null;
    formData.value.originalInspectionFee = undefined;
    
    // 清空时也触发校验
    nextTick(() => {
      if (formRef.value) {
        formRef.value.validateField('serviceFee');
        formRef.value.validateField('serviceLevelId');
      }
    });
  }
};

const bedOptions = ref([]);
// props设置
const bedProps = ref({
  multiple: false,
  expandTrigger: 'hover',
});

const props = defineProps({
  configInfo: {
    type: Object,
    default: () => ({}),
  },
  type: {
    type: String,
    default: '',
  },
});

const settleInTime = ref([]);
const feeTime = ref([]);

watch(
  props,
  () => {
    if (props.configInfo && Object.keys(props.configInfo).length > 0) {
      formData.value = { ...formData.value, ...props.configInfo };
      
      // 设置原始费用值（用于编辑时）
      if (props.configInfo.serviceFee) {
        originalInspectionFee.value = props.configInfo.serviceFee;
        formData.value.originalInspectionFee = props.configInfo.serviceFee;
      }
      if (props.configInfo.siteFee) {
        originalPointFee.value = props.configInfo.siteFee;
        formData.value.originalPointFee = props.configInfo.siteFee;
      }
      
      if (props.configInfo.startDate && props.configInfo.endDate) {
        settleInTime.value = [
          props.configInfo.startDate,
          props.configInfo.endDate,
        ];
      }
      if (props.configInfo.feeStartDate && props.configInfo.feeEndDate) {
        feeTime.value = [
          props.configInfo.feeStartDate,
          props.configInfo.feeEndDate,
        ];
      }
      if (props.configInfo.pointId) {
        formData.value.point = [
          props.configInfo.buildingId,
          props.configInfo.roomId,
          props.configInfo.pointId,
        ];
      }
    }
  },
  { immediate: true, deep: true },
);

// 监听价格变化
const textBlurPrice = (val, num) => {
  let data = null
  if (num === 1) {
    data = Number(formData.value.serviceFee)
    formData.value.serviceFee = data < 0 ? 0 : data
  } else if (num === 2) {
    data = Number(formData.value.siteFee)
    formData.value.siteFee = data < 0 ? 0 : data
  } else if (num === 3) {
    data = Number(formData.value.otherFees)
    formData.value.otherFees = data < 0 ? 0 : data
    // 最大值设置
    if (data > 99999999) {
      formData.value.otherFees = 99999999
    }
  } else if (num === 4) {
    data = Number(formData.value.insurancePayment)
    formData.value.insurancePayment = data < 0 ? 0 : data
    // 最大值设置
    if (data > 99999999) {
      formData.value.insurancePayment = 99999999
    }
  } else {
    data = Number(formData.value.governmentSubsidy)
    formData.value.governmentSubsidy = data < 0 ? 0 : data
    // 最大值设置
    if (data > 99999999) {
      formData.value.governmentSubsidy = 99999999
    }
  }
}

// 提供校验方法
const validate = async () => {
  try {
    await formRef.value.validate();
    return true;
  } catch (error) {
    return false;
  }
};

// 获取表单数据
const getFormData = () => {
  return formData.value;
};

//声明父组件的方法
const emits = defineEmits(['getFormData']);
//监听数据变化，传递到父组件
watch(
  formData,
  (val) => {
    //调用父组件的方法
    emits('getFormData', val);
  },
  { deep: true, immediate: true },
);

//暴露表单校验的方法给外面，给父组件
defineExpose({
  validate,
  getFormData,
});
</script>
<style></style>