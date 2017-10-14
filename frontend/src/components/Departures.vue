<template>
  <div>
     <div class="departure__row" v-for="transport in transports" :key="transport.journeynumber"> 
      <div class="departure__row-left">
        <span class="departure__line">{{transport.linenumber}}</span>
        <span class="departure__destination" :title="transport.destination">{{transport.destination}}</span>
      </div>
      <div class="departure__row-right">{{transport.displaytime}}</div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'departures',
    props: ['transports'],
  };
</script>

<style lang="less" scoped>
  .departure {
    &__row {
      display: flex;
      justify-content: space-between;
      font-size: 2rem;
      line-height: 2rem;

      &:not(:last-child) {
        margin-bottom: 1.3rem;
      }
    }

    &__line {
      margin-right: 0.2em;
    }

    &__row-left {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      min-width: 200px;
    }

    &__row-right {
      min-width: 100px;
      margin-left: 0.2em;
    }
  }
</style>
