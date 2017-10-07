<template>
  <div class="container">
    <h1>Sök</h1>
    <h2>Lägg till hållplats</h2>
    <el-form class="form" v-model="form" label-width="120px">
      <el-form-item label="Namn">
        <el-autocomplete
          class="form__input"
          placeholder="Hållplats..." 
          v-model="form.stop.name"
          :trigger-on-focus="false"
          :fetch-suggestions="search"
          :props="{label: 'name', value: 'siteid'}"
          icon="search"
          @select="handleSelect">
        </el-autocomplete>
      </el-form-item>
      <el-form-item label="Trafikslag">
        <el-radio-group v-model="form.type">
          <el-radio label="Buses"><i class="form__icon fa fa-bus" aria-hidden="true"></i>Buss</el-radio>
          <el-radio label="Metros"><i class="form__icon fa fa-subway" aria-hidden="true"></i>Tunnelbana</el-radio>
          <el-radio label="Trains"><i class="form__icon fa fa-train" aria-hidden="true"></i>Tåg</el-radio>
          <el-radio label="Trams"><i class="form__icon fa fa-subway" aria-hidden="true"></i>Spårvagn</el-radio>
          <el-radio label="Ships"><i class="form__icon fa fa-ship" aria-hidden="true"></i>Båt</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="Tidsfönster">
        <el-input-number 
          class="form__input" 
          v-model="form.timeWindow" 
          :min="1" 
          :max="60" >
        </el-input-number>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">Spara</el-button>
        <el-button @click="cancel">Avbryt</el-button>
      </el-form-item>
    </el-form>
    <h2>Sparade hållplatser</h2>
    <list class="list" @edit="edit" @remove="remove">
      <list-item v-for="(item, index) in getSelectedItems()" :id="item._id" :key="index">{{item.stop.name}}</list-item>
    </list>
  </div>
</template>

<script>
  import { debounce } from 'lodash';
  import Vue from 'vue';
  import { mapMutations, mapGetters } from 'vuex';
  import List from './List';
  import ListItem from './ListItem';

  export default {
    name: 'config',
    data() {
      return {
        form: {
          stop: {
            name: '',
            siteid: 0,
          },
          type: 'Buses',
          timeWindow: 20,
        },
      };
    },
    components: {
      list: List,
      listItem: ListItem,
    },
    computed: {
      ...mapGetters(['getStopName']),
    },
    methods: {
      search: debounce(async (query, cb) => {
        const { body: results } = await Vue.http.get(`/api/v1/search?query=${query}`);
        cb(results);
      }, 350),
      handleSelect(item) {
        this.form.stop = item;
      },
      save() {
        this.$store.dispatch('save', { item: this.form });
        this.$notify({
          title: this.form.stop.name,
          message: 'Hållplatsdata uppdaterades',
          type: 'success',
        });
        this.clearForm();
      },
      cancel() {
        this.clearForm();
      },
      edit(id) {
        const editing = this.getSelectedItems().find(stop => stop._id === id);
        if (editing) {
          this.form = editing;
        }
      },
      remove(id) {
        this.$notify({
          title: this.getStopName(id),
          message: 'Hållplatsdata uppdaterades',
          type: 'success',
        });
        this.$store.dispatch('delete', { id });
      },
      clearForm() {
        this.form = {
          stop: {
            name: '',
            siteid: 0,
          },
          type: 'Buses',
          timeWindow: 20,
        };
      },
      ...mapMutations(['addStop', 'removeStop']),
      ...mapGetters(['getSelectedItems']),
    },
  };
</script>

<style lang="less" scoped>
  .form {

    &__icon {
      margin-right: 0.4em;
    }

    &__input {
      width: 100%;
    }
  }
</style>