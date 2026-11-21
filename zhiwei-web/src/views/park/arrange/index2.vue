<!--负责企业-->
<template>
  <div class="min-h content-layout">
    <div class="bg-wt">
      <!-- 楼栋tab导航 -->
      <SwitchBar
        ref="tabBar"
        :data="tabData"
        :route-type="routeType"
        @change-id="getBuildingId"
      ></SwitchBar>
      <!-- end -->
      <!-- 区域类型列表 -->
      <HouseList
        :base-data="roomVoList"
        :building-data="tabData"
        :route-type="routeType"
        @handleCheck="handleRoomCheck"
        @handleSelectOlder="handleSelectOlder"
        @handleSingleOlder="handleSingleOlder"
      ></HouseList>
      <!-- end -->
    </div>
  </div>
</template>
<script setup>
import useUserStore from '@/store/modules/user';
import {getAllUserList} from '@/api/system/user';
// 接口
import {getRoomPointWithNurList, listAllWithNurBuilding, setInspection,} from '@/api/park/building';
// 组件
// tab切换
import SwitchBar from '@/components/switchBar/SwitchBartop.vue';
// 区域类型列表
import HouseList from './components/List.vue';
// 设置

const { proxy } = getCurrentInstance();
// 点位添加编辑弹层
// ------定义变量------
const userStore = useUserStore();
const route = useRoute(); // 获取局部
// 楼栋变量
const tabBar = ref(null); // 楼栋的ref
const tabData = ref([]); // 定义tab切换数据
const roomVoList = ref([]);
const buildingId = ref(''); // 楼栋id
const floorIndex = ref(null);
// 房间变量
const checkVisible = ref(false); // 查看房间弹层显示/隐藏

const routeType = ref(null);

// 设置巡检员的企业
const selectOlder = ref([]);
const singleNurse = ref([]);
const formRef = ref(null);

// 巡检员下拉数据
const nurseList = ref([]);
// 默认加载数据
onMounted(() => {
  if (route.query.type !== undefined) {
    routeType.value = route.query.type;
  }
  getAllBuildingList();
  getAllUserListFunc();
});
// ------定义方法------
// 获取单个设置企业的巡检人数据
const handleSingleOlder = (val) => {
    console.log(val,1)
  singleNurse.value = val;
};
// 获取要设置的企业数据
const handleSelectOlder = (val) => {
    console.log(val)
  selectOlder.value = val;
};
// 给企业设置巡检员
const handleSubmit = async (val) => {
  const params = selectOlder.value.map((item) => {
    return { tenantId: item.tenantId, inspectionIds: val }
  })
  const res = await setInspection(params);
  if (res.code === 200) {
    proxy.$modal.msgSuccess('设置成功');
    getAllBuildingList();
    handleCheckRoomClose();
    formRef.value.handleClear();
  } else {
    MessagePlugin.error(res.msg);
  }
};
// 获取巡检员下拉数据
const getAllUserListFunc = async () => {
  const res = await getAllUserList({ deptId: '211' });
  nurseList.value = res.data;
  //   nurseList.value = res.data.map((item) => {
  //     return { value: item.id, label: item.nickName }
  //   })
};
// 获取楼栋数据
const getAllBuildingList = async () => {
  const res = await listAllWithNurBuilding();
  if (res.code === 200) {
    tabData.value = res.data;
    // 首次进来需要拿楼栋第一条数据的id来获取当前数据
    if (buildingId.value === '') {
      buildingId.value = tabData.value[0].id;
    }
    getList();
  }
};
// 根据不同的楼栋id房间信息
const getList = async () => {
  const res = await getRoomPointWithNurList(buildingId.value);
  if (res.code === 200) {
    roomVoList.value = res.data;
  }
};
// 获取楼栋id
const getBuildingId = (id, i) => {
  floorIndex.value = i;
  buildingId.value = id;

  getAllBuildingList();
  userStore.setBuildingInfo({});
};
// 查看房间
const handleRoomCheck = () => {
  // handleRoomData(id)
  checkVisible.value = true;
};
// 关闭查看房间
const handleCheckRoomClose = () => {
  checkVisible.value = false;
  selectOlder.value = [];
  singleNurse.value = [];
};
</script>
<style lang="less" src="./../building/index.scss"></style>
