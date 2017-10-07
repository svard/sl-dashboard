<template>
  <div>
     <div class="departure__row" v-for="transport in transports" :key="transport.journeynumber"> 
      <div>
        <span class="departure__line">{{transport.linenumber}}</span>
        <span>{{transport.destination}}</span>
      </div>
      <div>{{transport.displaytime}}</div>
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
  }
</style>
