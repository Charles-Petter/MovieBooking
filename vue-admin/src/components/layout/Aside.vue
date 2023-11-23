<template>
  <div class="aside">
    <div class="aside-header" style="padding-top: 30px">
      <img style="width: 40px; height: 40px;float: left" src="../../assets/img/logo.png" alt=""/>
      <div style="padding-left: 15px;padding-top: 8px;float: left">后台管理</div>
    </div>
    <div>
      <el-col style="padding-top: 30px">
        <el-menu
            class="menu"
            background-color="#242930"
            text-color="#FFFFFF"
            active-text-color="#409EFF">

          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-film"></i>
              <span>影视管理</span>
            </template>
            <el-menu-item-group>
              <router-link to="/film/list">
                <el-menu-item style="padding-left: 70px" index="2-1">电影列表</el-menu-item>
              </router-link>
              <router-link to="/film/arrange">
                <el-menu-item style="padding-left: 70px" index="2-4">院线排片</el-menu-item>
              </router-link>
              <router-link to="/film/add">
                <el-menu-item style="padding-left: 70px" index="2-2">新增电影</el-menu-item>
              </router-link>
              <router-link to="/film/poster">
                <el-menu-item style="padding-left: 70px" index="2-5">轮播海报</el-menu-item>
              </router-link>
            </el-menu-item-group>
          </el-submenu>



          <router-link v-if="role==='worker'" to="/setting">
            <el-menu-item index="7">
              <i class="el-icon-setting"></i>
              <span slot="title">个人设置</span>
            </el-menu-item>
          </router-link>

        </el-menu>
      </el-col>
    </div>
  </div>
</template>

<script>
export default {

  data() {
    return {
      role: localStorage.getItem('role')
    }
  }

}
</script>

<style scoped>
.aside {
  color: #FFFFFF;
}

.aside-header {
  padding: 21px;
  font-size: 18px;
  font-weight: 500;
}

.menu {
  font-size: 100px;
  border-right: 0;
  letter-spacing: 1px;
  font-weight: bold;
  font-family: "Helvetica Neue", Helvetica, Arial, "Microsoft Yahei", "Hiragino Sans GB", "Heiti SC", "WenQuanYi Micro Hei", sans-serif;
}

</style>
