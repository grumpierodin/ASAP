import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import Rules from "../views/Rules.vue";
import Message from "../views/SubmitAlertPage.vue";
import LoginPage from "../views/LoginPage";
import RegisterPage from "../views/RegisterPage";
Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/home",
    name: "Home",
    component: Home,
  },
  {
    path: "/event",
    name: "testmessage",
    component: Message,
  },
  {
    path: "/rules",
    name: "Rules",
    component: Rules,
  },
  {
    path: "/login",
    name: "login",
    component: LoginPage,
  },
  {
    path: "/register",
    name: "register",
    component: RegisterPage,
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue"),
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});
router.replace("/");

export default router;
