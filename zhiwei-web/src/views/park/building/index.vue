<!-- 点位列表 -->
<template>
  <div class="min-h content-layout">
    <div class="bg-wt">
      <!-- 楼栋tab导航 -->
      <SwitchBar
        ref="tabBar"
        :data="tabData"
        :floorIndex="floorIndex"
        @handle-building-edit="handleBuildingEdit"
        @handle-add="handleBuildingAdd"
        @change-id="getBuildingId"
      ></SwitchBar>
      <!-- end -->
      <!-- 新增房间按钮 -->
      <BuildingInfo @handle-add="handleRoomAdd"></BuildingInfo>
      <!-- end -->
      <!-- 区域类型列表 -->
      <div>
        <HouseList
          :room-voList="roomVoList"
          :building-data="tabData"
          @handle-edit="handleRoomEdit"
          @handle-delete="handleDelete"
          @handle-add="handlePointAdd"
          @handle-point-edit="handlePointEdit"
          @handle-check="handleRoomCheck"
        ></HouseList>
      </div>
      <!-- end -->
      <!-- 楼栋添加、编辑 -->
      <AddBuilding
        ref="building"
        :visible="floorVisible"
        :title="textVal"
        :min-number="minNum"
        :max-number="maxNum"
        :data="floorData"
        :room-voList="roomVoList"
        :is-room-delete="isRoomDelete"
        @handle-delete="handleBuildingDelete"
        @handle-close="handleBuildingClose"
        @handle-add="handleBuildingAddSubmit"
        @handle-edit="handleBuildingEditSubmit"
      ></AddBuilding>
      <!-- end -->
      <!-- 房间添加、编辑 -->
      <AddRoom
        ref="room"
        :visible="roomVisible"
        :title="textVal"
        :min-number="minNum"
        :max-number="maxNum"
        :data="roomData"
        :room-type-data="areaTypeData"
        @handle-close="handleRoomClose"
        @handle-add="handleRoomAddSubmit"
        @handle-edit="handleRoomEditSubmit"
      ></AddRoom>
      <!-- end -->
      <!-- 点位添加、编辑 -->
      <AddPoint
        ref="point"
        :visible="bedVisible"
        :title="textVal"
        :min-number="minNum"
        :max-number="maxNum"
        :data="bedData"
        @handle-close="handlePointClose"
        @handle-add="handlePointAddSubmit"
        @handle-edit="handlePointEditSubmit"
      ></AddPoint>
      <!-- end -->
      <!-- 删除 -->
      <Delete
        :visible="deleteVisible"
        :delete-text="operateText"
        @handle-close="handleDeleteClose"
        @handle-delete="handleDeleteSubmit"
      ></Delete>
      <!-- end -->
    </div>
  </div>
</template>

<script setup name="Building">
const { proxy } = getCurrentInstance();
import useUserStore from '@/store/modules/user';
// 接口
// 区域类型
import {getAreaTypesList} from '@/api/park/areaType';
// 楼栋
import {
  addPoint,
  addBuilding,
  addRoom,
  delPoint,
  delBuilding,
  delRoom,
  getPointDetails,
  getBuildingDetails,
  getRoomPointList,
  getRoomDetails,
  listBuilding,
  updatePoint,
  updateBuilding,
  updateRoom,
} from '@/api/park/building';
// 组件
// tab切换
import SwitchBar from './components/SwitchBartop.vue';
// 区域类型列表
import HouseList from './components/List.vue';
// 新增房间按钮
import BuildingInfo from './components/BuildingInfo.vue';
// // 查看放假弹层
// import CheckRoom from './components/CheckRoom.vue'
// 新增/编辑楼栋弹层
import AddBuilding from './components/AddBuilding.vue';
// 房间添加编辑弹层
import AddRoom from './components/AddRoom.vue';
// 点位添加编辑弹层
import AddPoint from './components/AddPoint.vue';
// 删除弹层
import Delete from '@/components/DelectDialog/index.vue'
// 定义变量
const userStore = useUserStore();
const router = useRouter(); // 获取全局
const route = useRoute(); // 获取局部
const textVal = ref(''); // 弹层标题
// 楼栋变量******
const tabBar = ref(null); // 楼栋的ref
const tabData = ref([]); // 定义tab切换数据
const floorData = ref({}); // 楼栋基本信息
const buildingId = ref(''); // 楼栋id
const floorVisible = ref(false); // 添加、编辑楼栋弹层显示/隐藏
const minNum = ref(1); // 最小的排序号
const maxNum = ref(99999999); // 最大的排序号
const isOperateSuccess = ref(false); // 是否添加成功
const floorIndex = ref(null);
const isRoomDelete = ref(false); // 判断楼栋下是否有房间，如果有删除按钮置灰
const deleteBuildingId = ref(''); // 要删除的楼栋id
const building = ref(null);
// 房间变量******
const roomVoList = ref([]); //房间数据
const roomId = ref(''); // 房间id
const roomVisible = ref(false); // 添加、编辑房间弹层显示/隐藏
// const checkVisible = ref(false); // 查看房间弹层显示/隐藏
const roomData = ref({}); // 房间表单信息
const areaTypeData = ref([]); // 所有房间类型
const room = ref(null);
// 点位变量******
const point = ref(null);
const pointId = ref(''); // 房间id
const bedVisible = ref(false); // 添加、编辑房间弹层显示/隐藏
const bedData = ref({}); // 点位表单数据
const bedSelectData = ref({}); // 选择的点位信息
const tabCheck = ref(false); // 是否触发了tab切换
// 删除弹层
const deleteVisible = ref(false); // 删除弹层显示/隐藏
const operateText = ref(''); // 删除信息
const delectType = ref(null); // 需要删除的类型，因为楼栋、房间和点位用的是一个删除弹窗
// 默认加载数据
onMounted(() => {
  getAllBuildingList();
  getHouseType();
});
// 定义方法
// 楼栋******************************************
// 获取楼栋数据
const getAllBuildingList = async () => {
  const res = await listBuilding();
  if (res.code === 200) {
    tabData.value = res.data;
    const floorBase = userStore.floorInfo;
    // 判断是否添加楼栋成功，如果成功需要拿当前数据的id来获取当前的楼栋数据
    if (tabData.value.length > 0) {
      tabData.value.forEach((val, index) => {
        // 新增编辑楼栋逻辑
        if (floorBase.code) {
          if (floorBase.code > val.code) {
            tabBar.value.currentId = index + 1;
            buildingId.value = tabData.value[index + 1].id;
          } else if (
            (floorBase.code === val.code && floorBase.name === val.name) ||
            floorBase.id === val.id
          ) {
            tabBar.value.currentId = index;
            buildingId.value = tabData.value[index].id;
          }
        } else {
          // 删除楼栋逻辑
          if (index === floorIndex.value) {
            buildingId.value = tabData.value[floorIndex.value].id;
          }
          if (isOperateSuccess.value && buildingId.value !== val.id) {
            tabBar.value.currentId = floorIndex.value;
            // 如果删除的是最后一条，设置当前选中状态（要删除的当前状态值-1），同理当前的楼栋数据设置为最后一条
            // floorIndex.value是要删除的楼栋索引值，从子元素拿去
            if (tabData.value.length === floorIndex.value) {
              tabBar.value.currentId = floorIndex.value - 1; // 获取现有的tab选中状态
              buildingId.value = tabData.value[floorIndex.value - 1].id;
            }
            // 删除的是第一条逻辑
            if (index === floorIndex.value) {
              buildingId.value = tabData.value[floorIndex.value].id;
            }
            isOperateSuccess.value = false;
          }
        }
      });
    }
    // 首次进来需要拿楼栋第一条数据的id来获取当前数据
    if (buildingId.value === '') {
      buildingId.value = tabData.value[0].id;
    }
    getRoomListData();
  }
};
// 编辑楼栋、获取楼栋id
const handleBuildingEdit = (id, i) => {
  floorIndex.value = i;
  buildingId.value = id;
  floorVisible.value = true;

  handleBuildingData(buildingId.value);
  userStore.setBuildingInfo({});
  textEdit();
};

// 新增楼栋弹层
const handleBuildingAdd = () => {
  floorData.value = {
    code: 1,
  };
  textAdd();
  floorVisible.value = true;
};
// 关闭楼栋弹层
const handleBuildingClose = () => {
  floorVisible.value = false;
};
// 楼栋点击删除时显示删除弹层
const handleBuildingDelete = (id) => {
  buildingId.value = '';
  deleteBuildingId.value = id;
  handleDelete();
};
// 获取楼栋id
const getBuildingId = (id, i, isCheckTab) => {
  floorIndex.value = i;
  buildingId.value = id;
  getAllBuildingList();
  userStore.setBuildingInfo({});
};

// 获取楼栋详情
const handleBuildingData = async (id) => {
  const res = await getBuildingDetails(id);
  if (res.code === 200) {
    floorData.value = res.data;
  }
};
// 新增楼栋提交
const handleBuildingAddSubmit = async (val) => {
  const res = await addBuilding(val);
  if (res.code === 200) {
    getAllBuildingList();
    handleBuildingClose();
    building.value.handleClear();
    proxy.$modal.msgSuccess('新增成功');
  }
};
// 编辑楼栋提交
const handleBuildingEditSubmit = async (val) => {
  const params = {
    id: floorData.value.id,
    code: val.code,
    name: val.name,
  };
  const res = await updateBuilding(params);

  if (res.code === 200) {
    getAllBuildingList();
    handleBuildingClose();
    building.value.handleClear();
    proxy.$modal.msgSuccess('编辑成功');
  }
};

// 删除楼栋
const handleBuildingDeleteSubmit = async () => {
  isOperateSuccess.value = false;
  const res = await delBuilding(deleteBuildingId.value);
  if (res.code === 200) {
    userStore.setBuildingInfo({});
    isOperateSuccess.value = true;
    building.value.handleClear();
    getAllBuildingList(); // 刷新列表
    floorVisible.value = false; // 隐藏楼栋弹层
    handleDeleteClose(); // 关闭删除弹层
  }
};
// 房间******************************************
// 根据不同的楼栋id获取房间数据
const getRoomListData = async () => {
  const res = await getRoomPointList(buildingId.value);
  if (res.code === 200) {
    roomVoList.value = res.data;
  }
};
// 获取房间类型
const getHouseType = async () => {
  const res = await getAreaTypesList();
  if (res.code === 200) {
    areaTypeData.value = res.data.filter((val) => val.status === 1);
  }
};
// 新增房间提交
const handleRoomAddSubmit = async (val) => {
  const params = {
    ...val,
    buildingId: buildingId.value,
  };
  const res = await addRoom(params);
  if (res.code === 200) {
    getRoomListData(); // 刷新列表
    handleRoomClose(); // 隐藏房间弹层
    room.value.handleClear();
    proxy.$modal.msgSuccess('新增成功');
  }
};
// 获取房间详情
const handleRoomData = async (id) => {
  const res = await getRoomDetails(id);
  if (res.code === 200) {
    roomData.value = res.data;
  }
};
// 编辑房间提交
const handleRoomEditSubmit = async (val) => {
  const res = await updateRoom(val);
  if (res.code === 200) {
    getRoomListData();
    handleRoomClose(); // 隐藏房间弹层
    room.value.handleClear();
    proxy.$modal.msgSuccess('编辑成功');
  }
};
// 删除房间提交
const handleRoomDeleteSubmit = async () => {
  const res = await delRoom(roomId.value);
  if (res.code === 200) {
    getRoomListData();
    handleDeleteClose();
  }
};
// 新增房间
const handleRoomAdd = () => {
  textAdd();
  roomVisible.value = true;
};
// 编辑房间
const handleRoomEdit = (id) => {
  textEdit();
  roomId.value = id;
  handleRoomData(id); // 打开编辑，获取房间详情
  roomVisible.value = true;
};
// // 查看房间
// const handleRoomCheck = (id) => {
//   handleRoomData(id);
//   checkVisible.value = true;
// };
// // 关闭查看房间
// const handleCheckRoomClose = () => {
//   room.value.handleClear();
//   checkVisible.value = false;
// };
// 关闭房间弹层
const handleRoomClose = () => {
  roomVisible.value = false;
};
// 点位******************************************
// 新增点位提交
const handlePointAddSubmit = async (val) => {
  const params = {
    ...val,
    roomId: roomId.value,
  };
  const res = await addPoint(params);
  if (res.code === 200) {
    getRoomListData();
    handlePointClose();
    point.value.handleClear();
    proxy.$modal.msgSuccess('新增成功');
  }
};
// 编辑点位提交
const handlePointEditSubmit = async (val) => {
  const params = {
    ...val,
    roomId: roomId.value,
  };
  const res = await updatePoint(params);
  if (res.code === 200) {
    getRoomListData();
    handlePointClose();
    point.value.handleClear();
    proxy.$modal.msgSuccess('编辑成功');
  }
};
// 获取点位详情
const handlePointData = async (id) => {
  const res = await getPointDetails(id);
  if (res.code === 200) {
    bedData.value = res.data;
  }
};
// 删除点位提交
const handlePointDeleteSubmit = async () => {
  const res = await delPoint(pointId.value);
  if (res.code === 200) {
    getRoomListData();
    handleDeleteClose();
  }
};
// 点位新增
const handlePointAdd = (id) => {
  roomId.value = id;
  textAdd();
  bedVisible.value = true;
};
// 点位编辑
const handlePointEdit = (id) => {
  textEdit();
  pointId.value = id;
  handlePointData(id); // 打开编辑，获取点位详情
  bedVisible.value = true;
};
// 点位关闭
const handlePointClose = () => {
  bedVisible.value = false;
};
// 公用方法
// 新增文本
const textAdd = () => {
  textVal.value = '新增';
};
// 编辑文本
const textEdit = () => {
  textVal.value = '编辑';
};
// 显示删除弹层
const handleDelete = (type, val) => {
  // 用type来判断删除的是房间还是点位，因为他们公用了一个删除 弹层
  delectType.value = type;
  if (type === 2) {
    roomId.value = val.id;
    operateText.value = '房间';
  } else if (type === 3) {
    pointId.value = val.id;
    operateText.value = '点位';
  } else {
    operateText.value = '楼栋';
  }
  // deleteVisible.value = true;
  proxy.$modal
    .confirm('此操作将删除该' + operateText.value + '，是否继续？')
    .then(function () {
      return handleDeleteSubmit();
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
};
// 关闭删除弹层
const handleDeleteClose = () => {
  deleteVisible.value = false;
};
// 删除提交
const handleDeleteSubmit = () => {
  const type = delectType.value
  if (type === 2) {
    handleRoomDeleteSubmit()
  } else if (type === 3) {
    handlePointDeleteSubmit()
  } else {
    handleBuildingDeleteSubmit()
  }
}
</script>
<style lang="less" src="./index.scss"></style>
