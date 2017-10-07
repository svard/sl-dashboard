<template>
  <li class="list-item">
    <slot></slot>
    <span class="list-item__options">
      <i class="list-item__icon el-icon-edit" @click="onEdit"></i>
      <i class="list-item__icon el-icon-close" @click="onRemove"></i>
    </span>
  </li>
</template>

<script>
  export default {
    name: 'list-item',
    props: ['id'],
    methods: {
      onEdit() {
        this.$parent.$emit('edit', this.id);
      },
      onRemove() {
        this.$parent.$emit('remove', this.id);
      },
    },
  };
</script>

<style lang="less" scoped>
  .list-item {
    padding: 0.25em 0;

    &__options {
      float: right;
    }

    &__icon {
      cursor: pointer;
      margin-right: 0.4em;
      opacity: .7;

      &:hover {
        opacity: 1;
      }

      &:active {
        opacity: .4;
      }
    }
  }
</style>