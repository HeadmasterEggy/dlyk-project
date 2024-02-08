<template>
  <el-button type="primary">添加用户</el-button>
  <el-button type="danger">批量删除</el-button>

  <el-table
      :data="userList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
  >
    <el-table-column type="selection" width="55"/>
    <el-table-column type="index" label="序号" width="120"/>
    <el-table-column property="loginAct" label="账号" width="120"/>
    <el-table-column property="name" label="姓名" show-overflow-tooltip/>
    <el-table-column property="phone" label="联系方式" show-overflow-tooltip/>
    <el-table-column property="email" label="邮箱" show-overflow-tooltip/>
    <el-table-column property="createTime" label="创建时间" show-overflow-tooltip/>

    <el-table-column label="操作" show-overflow-tooltip>
      <template #default="scope">
        <el-button type="primary" @click="view(scope.row.id)">详情</el-button>
        <el-button type="success" @click="edit(scope.row.id)">编辑</el-button>
        <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
      </template>

    </el-table-column>

  </el-table>

  <el-pagination
      background
      layout="prev, pager, next"
      :page-size="pageSize"
      :total="total"
      @prev-click="toPage"
      @current-change="toPage"
      @next-click="toPage"
  />
</template>

<script>
import {defineComponent} from "vue";
import {doGet} from "../http/httpRequest.js";

export default defineComponent({
  name: "UserView",
  data() {
    return {
      //用户列表数据，初始值是空
      userList: [{}],
      //分页时每页显示多少条数据
      pageSize: 0,
      //分页总共查询出多少条数据
      total: 0
    }
  },

  mounted() {
    this.getData(1);
  },

  methods: {
    //勾选或取消勾选时触发该函数
    handleSelectionChange() {

    },


    //查询用户列表数据
    getData(current) {
      doGet("/api/users", {
        //当前查询第几页
        current: current
      }).then(resp => {
        console.log(resp);
        if (resp.data.code === 200) {
          this.userList = resp.data.data.list;
          this.pageSize = resp.data.data.pageSize;
          this.total = resp.data.data.total;
        }
      })
    },

    toPage(current) {
      this.getData(current);
    },

    view(id) {
      console.log(this.userList);
      this.$router.push("/dashboard/user/" + id);
    }
  }
})

</script>


<style scoped>

</style>
