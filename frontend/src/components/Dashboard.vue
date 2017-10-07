<template>
  <div class="container">
    <h1>Avgångar</h1>
    <main class="content">
      <section class="content__section" v-for="(item, index) in getSelectedItems()" :key="index">
        <div class="sectionHeading">
          <h2>{{item.stop.name}}<i class="sectionHeading__icon fa" :class="classes(item)" aria-hidden="true"></i></h2>
        </div>
        <departures :transports="item.departures"></departures>
      </section>
    </main>
  </div>
</template>

<script>
  import { mapGetters, mapMutations } from 'vuex';
  import Departures from './Departures';

  export default {
    name: 'dashboard',
    created() {
      this.clearStopData();
      this.getSelectedItems().forEach((stop) => {
        this.$store.dispatch('departures', {
          siteId: stop.stop.siteid,
          type: stop.type,
          timeWindow: stop.timeWindow,
        });
      });
    },
    components: {
      departures: Departures,
    },
    methods: {
      ...mapGetters([
        'getSelectedItems',
      ]),
      ...mapMutations([
        'clearStopData',
      ]),
      classes(stop) {
        return {
          'fa-subway': stop.type === 'Metros' || stop.type === 'Trams',
          'fa-bus': stop.type === 'Buses',
          'fa-train': stop.type === 'Trains',
          'fa-ship': stop.type === 'Ships',
        };
      },
    },
  };
</script>

<style lang="less" scoped>
  .content {
    @media (min-width: 961px) {
      display: flex;
      flex-wrap: wrap;
    }

    &__section {
      width: 100%;
      margin-bottom: 2em;

      @media (min-width: 961px) {
        width: 50%;
        margin-bottom: 2em;

        &:nth-child(odd) {
          padding-right: 2em;
        }
      }
    }
  }

  .sectionHeading {
    border-bottom: 1px solid var(--black);
    margin-bottom: 0.5em;

    &__icon {
      float: right;
    }

    h2 {
      margin: 0.3em 0;
    }
  }
</style>