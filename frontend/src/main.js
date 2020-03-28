import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import Vue from 'vue'
import App from './App.vue'
import { BootstrapVue } from 'bootstrap-vue'
import router from './router'

Vue.use(BootstrapVue);
Vue.config.productionTip = false;
Vue.prototype.url = "http://" + window.location.hostname + ":8080";

const app = new Vue({
  router,
  render: h => h(App)
}).$mount('#app');

window.addEventListener('popstate', () => {
  app.currentRoute = window.location.pathname
});
