<template>
  <el-form ref="loginRefForm" :model="userDetail" label-width="120px">
    <el-form-item label="ID">{{ userDetail.id }}</el-form-item>

    <el-form-item label="账号"> {{ userDetail.loginAct }}</el-form-item>

    <el-form-item label="密码"> {{ userDetail.loginPwd }}</el-form-item>

    <el-form-item label="姓名">{{ userDetail.name }}</el-form-item>

    <el-form-item label="联系方式">{{ userDetail.phone }}</el-form-item>

    <el-form-item label="邮箱">{{ userDetail.email }}</el-form-item>

    <el-form-item label="账号未过期"> {{ userDetail.accountNoExpired === 1 ? '是' : '否' }}</el-form-item>

    <el-form-item label="密码未过期"> {{ userDetail.credentialsNoExpired === 1 ? '是' : '否' }}</el-form-item>

    <el-form-item label="账号未锁定"> {{ userDetail.accountNoLocked === 1 ? '是' : '否' }}</el-form-item>

    <el-form-item label="账号启用"> {{ userDetail.accountEnabled === 1 ? '是' : '否' }}</el-form-item>

    <el-form-item label="创建时间"> {{ userDetail.createTime }}</el-form-item>

    <el-form-item label="创建人"> {{ userDetail.createBy }}</el-form-item>

    <el-form-item label="编辑时间"> {{ userDetail.editTime }}</el-form-item>

    <el-form-item label="编辑人"> {{ userDetail.editBy }}</el-form-item>

    <el-form-item label="最近登录时间"> {{ userDetail.lastLoginTime }}</el-form-item>

    <el-form-item>
      <el-button type="primary" @click="goBack">返 回</el-button>
    </el-form-item>
  </el-form>
</template>


<script>
import {defineComponent} from "vue";
import {doGet} from "../http/httpRequest.js";

export default defineComponent({
  name: "UserDetailView",

  data() {
    return {
      userDetail: {},
    }
  },

  mounted() {
    this.loadUserDetail();
  },

  methods: {
    loadUserDetail() {
      //id要和动态路由中配置的path id相同
      let id = this.$route.params.id;
      doGet("/api/user" + id, {}).then(resp => {
        if (resp.data.code === 200) {
          this.userDetail = resp.data.data;
        }
      })
    },

    //返回
    goBack() {
      this.$router.go(-1);
    }
  }
})
</script>

<style scoped>

</style>
