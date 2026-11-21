<template>
  <div class="app-container">
    <div class="searchBox">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="企业名称" prop="tenantName">
        <el-input
          v-model="queryParams.tenantName"
          placeholder="请输入"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="企业统一社会信用代码" prop="creditCode">
        <el-input
          v-model="queryParams.creditCode"
          placeholder="请输入"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    </div>
    

    <el-row :gutter="10" class="newBtn mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="Plus"
          @click="toDetails"
          v-hasPermi="['park:settleIn:add']"
        >发起入驻申请</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="settleInList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" align="center" type="index" min-width="60px" />
      <el-table-column label="企业名称" align="center" prop="tenantName" min-width="120px" />
      <el-table-column label="企业统一社会信用代码" align="center" prop="creditCode"  min-width="200px"/>
      <el-table-column label="入驻点位" align="center" prop="pointCode"  min-width="100px"/>
      <el-table-column label="服务等级" align="center" prop="serviceLevelName"  min-width="120px"/>
      <el-table-column label="入驻期限" align="center" prop="startDate"  min-width="200px">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }} ~ {{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="状态" align="center" prop="status" >
        <template #default="scope">
          <el-tag v-if="scope.row.status === 0" type="success">已入驻</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="danger">已退住</el-tag>
        </template>
      </el-table-column> -->
      <el-table-column label="创建时间" align="center" prop="startDate" min-width="200px">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" 
          @click="getDetails(scope.row, 'read')" v-hasPermi="['tenant:settleIn:edit']">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script setup name="SettleIn">
import {listSettleIn} from "@/api/park/settleIn";

const { proxy } = getCurrentInstance();

const settleInList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);

const route = useRoute();

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tenantName: null,
    tenantId: null,
    creditCode: null,
    startDate: null,
    endDate: null,
    serviceLevelName: null,
    pointCode: null,
    status: null,
    sortOrder: null,
  },
  rules: {
    tenantName: [
      { required: true, message: "企业名称为空，请输入企业名称", trigger: "blur" }
    ],
    tenantId: [
      { required: true, message: "企业ID为空，请输入企业ID", trigger: "blur" }
    ],
    creditCode: [
      { required: true, message: "统一社会信用代码为空，请输入统一社会信用代码", trigger: "blur" }
    ],
    startDate: [
      { required: true, message: "入驻开始时间为空，请输入入驻开始时间", trigger: "blur" }
    ],
    serviceLevelName: [
      { required: true, message: "服务等级名称为空，请输入服务等级名称", trigger: "blur" }
    ],
    pointCode: [
      { required: true, message: "入驻点位为空，请输入入驻点位", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态 (0: 已入驻, 1: 已退住)不能为空", trigger: "change" }
    ],
    sortOrder: [
      { required: true, message: "排序编号为空，请输入排序编号", trigger: "blur" }
    ],
    createTime: [
      { required: true, message: "创建时间为空，请输入创建时间", trigger: "blur" }
    ],
    updateTime: [
      { required: true, message: "修改时间为空，请选择修改时间", trigger: "blur" }
    ],
    createBy: [
      { required: true, message: "创建人为空，请输入创建人", trigger: "blur" }
    ],
    updateBy: [
      { required: true, message: "修改人为空，请输入修改人", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

const toDetails = () => {
  proxy.$router.push({ path: "/enterQuit/settleInInfo" });
}

/** 查询入驻列表 */
function getList() {
  loading.value = true;
  listSettleIn(queryParams.value).then(response => {
    settleInList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

//页面跳转到详情页
const getDetails = (row, type) => {
  proxy.$router.push({ path: "/enterQuit/detailsLook", query: { id: row.id, type: type } });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    tenantName: null,
    tenantId: null,
    creditCode: null,
    startDate: null,
    endDate: null,
    serviceLevelName: null,
    pointCode: null,
    status: null,
    sortOrder: null,
    createTime: null,
    updateTime: null,
    createBy: null,
    updateBy: null,
    remark: null
  };
  proxy.resetForm("settleInRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

//解决页面不刷新的问题
watch(route,(to,from) => {
  proxy.$router.go(0);
})
getList();
</script>
