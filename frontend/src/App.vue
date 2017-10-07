<template>
  <div id="app">
    <el-menu :default-active="activeIndex" mode="horizontal" router>
      <el-menu-item index="/"><img src="./assets/sl_logo.svg" class="menu__logo" width="36" height="30" /></el-menu-item>
      <el-menu-item index="/config">Hållplatser</el-menu-item>
    </el-menu>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app',
  data() {
    return {
      activeIndex: '/',
    };
  },
};
</script>

<style lang="less" scoped>
.menu {
  &__logo {
    vertical-align: middle;
  }
}
</style>
