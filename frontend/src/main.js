import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import Global from './plugins/globalComponents'
import router from './router'
import Vue from 'vue'
import App from './App.vue'
import { BootstrapVue, BootstrapVueIcons } from 'bootstrap-vue'

Vue.use(Global)
Vue.use(BootstrapVue);
Vue.use(BootstrapVueIcons);

Vue.config.productionTip = false;
Vue.prototype.serverUrl = "http://" + window.location.hostname + ":8080/backend";

const app = new Vue({
  router,
  render: h => h(App)
}).$mount('#app');

window.addEventListener('popstate', () => {
  app.currentRoute = window.location.pathname
});

module.exports = {
  publicPath: process.env.NODE_ENV === 'production'
      ? '/dist/'
      : '/'
}
