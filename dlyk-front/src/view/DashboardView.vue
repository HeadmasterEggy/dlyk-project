<template>
  <el-container>
    <el-aside :width="isCollapse ? '64px' : '200px'">

      <div class="menuTitle">动力云客系统</div>

      <el-menu
          :collapse="isCollapse"
          :collapse-transition="false"
          :unique-opened="true"
          class="el-menu-vertical-demo"
          default-active="2"
          style="border-right: solid 0px;"
      >
        <el-sub-menu index="1">

          <template #title>
            <el-icon>
              <OfficeBuilding/>
            </el-icon>
            <span>市场活动</span>
          </template>
          <el-menu-item index="1-1">市场活动</el-menu-item>
          <el-menu-item index="1-2">市场统计</el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="2">

          <template #title>
            <el-icon>
              <Paperclip/>
            </el-icon>
            <span>线索管理</span>
          </template>
          <el-menu-item index="1-1">线索管理</el-menu-item>
          <el-menu-item index="1-2">线索统计</el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="3">

          <template #title>
            <el-icon>
              <Avatar/>
            </el-icon>
            <span>客户管理</span>
          </template>
          <el-menu-item index="1-1">客户管理</el-menu-item>
          <el-menu-item index="1-2">客户统计</el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="4">

          <template #title>
            <el-icon>
              <Coin/>
            </el-icon>
            <span>交易管理</span>
          </template>
          <el-menu-item index="1-1">交易管理</el-menu-item>
          <el-menu-item index="1-2">交易统计</el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="5">

          <template #title>
            <el-icon>
              <Memo/>
            </el-icon>
            <span>产品管理</span>
          </template>
          <el-menu-item index="1-1">产品管理</el-menu-item>
          <el-menu-item index="1-2">产品统计</el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="6">

          <template #title>
            <el-icon>
              <Collection/>
            </el-icon>
            <span>字典管理</span>
          </template>
          <el-menu-item index="1-1">字典管理</el-menu-item>
          <el-menu-item index="1-2">字典统计</el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="7">

          <template #title>
            <el-icon>
              <User/>
            </el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="1-1">用户管理</el-menu-item>
          <el-menu-item index="1-2">用户统计</el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="8">

          <template #title>
            <el-icon>
              <Menu/>
            </el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="1-1">系统管理</el-menu-item>
          <el-menu-item index="1-2">系统统计</el-menu-item>

        </el-sub-menu>

      </el-menu>

    </el-aside>

    <el-container class="rightContent">
      <el-header>
        <el-icon class="show" @click="showMenu">
          <Fold/>
        </el-icon>

        <el-dropdown trigger="click">
        <span class="el-dropdown-link">
          {{ user.name }}
          <el-icon class="el-icon--right"><arrow-down/></el-icon>
        </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>我的资料</el-dropdown-item>
              <el-dropdown-item>修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

      </el-header>
      <el-main>Main</el-main>
      <el-footer>Footer</el-footer>
    </el-container>
  </el-container>
</template>

<script>
import {
  ArrowDown,
  Avatar, Check, CircleCheck, CirclePlus, CirclePlusFilled,
  Coin,
  Collection,
  Fold,
  Memo,
  OfficeBuilding,
  Paperclip, Plus,
  User
} from "@element-plus/icons-vue";
import {defineComponent} from "vue";
import {doGet} from "../http/httpRequest.js";
import {messageConfirm, messageTip, removeToken} from "../util/utils.js";

export default defineComponent({
  name: "DashboardView",

  data() {
    return {
      isCollapse: false,

      //登录用户对象
      user: {}
    }
  },

  mounted() {
    this.loadLoginUser();
  },

  methods: {
    showMenu() {
      this.isCollapse = !this.isCollapse;
    },

    loadLoginUser() {
      doGet("/api/login/info", {}).then((resp) => {
        console.log(resp)
        this.user = resp.data.data;
      })
    },

    logout() {
      doGet("/api/logout", {}).then(resp => {
        if (resp.data.code === 200) {
          removeToken();
          messageTip("退出成功", "success");
          window.location.href = "/";
        } else {
          messageConfirm("退出异常，是否强制退出").then(() => {
            removeToken();
            window.location.href = "/";
          }).catch(() => {
            messageTip("取消强制退出", "warning");
          })
        }
      })
    }

  },
  components: {ArrowDown, Fold, Avatar, Collection, Memo, Coin, User, Paperclip, OfficeBuilding}
})
</script>

<style scoped>
.el-aside {
  background: #1a1a1a;
}

.el-header {
  background: cyan;
  height: 35px;
  line-height: 35px;
}

.el-footer {
  background: cyan;
  height: 35px;
  text-align: center;
  line-height: 35px;
}

.rightContent {
  height: calc(100vh);
}

.menuTitle {
  height: 35px;
  background: white;
  text-align: center;
  line-height: 35px;
}

.show {
  cursor: pointer;
}

.el-dropdown {
  float: right;
  text-align: center;
  line-height: 35px;
}
</style>
