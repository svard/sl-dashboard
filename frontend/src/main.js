import Vue from 'vue';
import ElementUI from 'element-ui';
import VueResource from 'vue-resource';
import locale from 'element-ui/lib/locale/lang/en';
import 'normalize.css';
import 'element-ui/lib/theme-default/index.css';
import 'font-awesome/css/font-awesome.css';
import App from './App';
import router from './router';
import store from './store';
import './assets/css/base.css';

Vue.config.productionTip = false;

Vue.use(ElementUI, { locale });
Vue.use(VueResource);

Vue.http.get('/api/v1/select').then(({ body: items }) => {
  /* eslint-disable no-new */
  new Vue({
    el: '#app',
    store: store(items),
    router,
    render: h => h(App),
  });
});
