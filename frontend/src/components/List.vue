<template>
  <ul class="list">
    <slot></slot>
  </ul>
</template>

<script>
  export default {
    name: 'list',
  };
</script>

<style lang="less" scoped>
  .list {
    list-style: none;
    margin: 0;
    padding: 0;
  }
</style>