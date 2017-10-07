import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const departureUrl = (siteId, type = 'Buses', timeWindow = 20) =>
  `/api/v1/departures/${siteId}?type=${type}&timewindow=${timeWindow}`;

const selectUrl = (id) => {
  if (id) {
    return `/api/v1/select/${id}`;
  }
  return '/api/v1/select';
};

const getters = {
  getSelectedItems(state) {
    return state.selectedItems;
  },
  getStopName(state) {
    return (id) => {
      const stop = state.selectedItems.find(
        stop => stop._id === id,
      );
      if (!stop) {
        return '';
      }
      return stop.stop.name;
    };
  },
};

export const mutations = {
  newDepartures(state, { siteId, data, type }) {
    state.selectedItems = state.selectedItems.map((item) => {
      if (item.stop.siteid === siteId && item.type === type) {
        item.departures = data[type.toLowerCase()];
        item.latestUpdate = data.latestupdate;
      }
      return item;
    });
  },
  clearStopData(state) {
    state.stopData = [];
  },
  addStop(state, { item }) {
    const exisitingStop = state.selectedItems.findIndex(
      selectedStop => selectedStop.stop.siteid === item.stop.siteid,
    );
    if (exisitingStop < 0) {
      state.selectedItems = [...state.selectedItems, item];
    } else {
      state.selectedItems.splice(exisitingStop, 1, item);
    }
  },
  setSelectedItems(state, { items }) {
    state.selectedItems = items;
  },
  removeStop(state, { id }) {
    state.selectedItems = state.selectedItems.filter(stop => stop._id !== id);
  },
};

const actions = {
  async departures({ commit }, { siteId, type, timeWindow }) {
    const { body: data } = await Vue.http.get(
      departureUrl(siteId, type, timeWindow),
    );
    commit('newDepartures', { siteId, data, type });
  },
  async save({ commit }, { item }) {
    item.stop.siteid = parseInt(item.stop.siteid, 10);
    const { body: data } = await Vue.http.post(selectUrl(), item);
    commit('addStop', { item: data });
  },
  async delete({ commit }, { id }) {
    await Vue.http.delete(selectUrl(id));
    commit('removeStop', { id });
  },
};

function builder(data) {
  return new Vuex.Store({
    state: {
      selectedItems: data,
    },
    getters,
    mutations,
    actions,
  });
}

export default builder;
