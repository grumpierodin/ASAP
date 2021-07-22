import Vue from "vue";
import App from "./App.vue";
import axios from "axios";
import router from "./router";
import store from "./store";
import eventBus from "./event-bus";
import realTimeClient from "./websocket/real-time-client";
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import "bootstrap-vue/dist/bootstrap-vue-icons.css";
import "./app.scss";

// Bootstrap axios
axios.defaults.baseURL = "/api";
axios.defaults.headers.common.Accept = "application/json";
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    return Promise.reject(error);
  }
);

Vue.config.productionTip = false;

Vue.prototype.$bus = eventBus;
Vue.prototype.$rt = realTimeClient;

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
