<template>
  <div id="app">
    <div id="nav">
      <router-link to="/about">About</router-link>
      |
      <router-link to="/home">Alerts</router-link>
      |
      <router-link to="/rules">Rules</router-link>
      |
      <router-link to="/event">Submit Event</router-link>
    </div>
    <router-view />
  </div>
</template>
<script>
import authenticationService from "@/services/authentication";
export default {
  name: "App",
  created() {
    this.$router.beforeEach((to, from, next) => {
      console.log(this);
      if (
        to.name !== "login" &&
        to.name !== "register" &&
        this.$store.getters.getToken.length < 5
      )
        next({ name: "login" });
      else next();
    });
    this.$bus.$off("RealTimeClient.loggedOut");
    this.$bus.$off("authenticated");
    this.$bus.$on("authenticated", () => {
      authenticationService
        .realTimeServer()
        .then((data) => {
          if (this.$rt._isConnected()) {
            this.$rt.logout();
          }
          console.log(data);
          console.log("Initializing the real time connection");
          this.$rt.init(
            this.$store.getters.getRealtimeServer,
            this.$store.getters.getToken
          );
        })
        .catch((error) => {
          console.log(JSON.stringify(error));
        });
    });
    this.$bus.$on("RealTimeClient.loggedOut", () => {
      this.$router.push({ path: "/login" });
      location.reload();
    });
  },
  destroyed() {
    this.$bus.$off("authenticated");
    this.$bus.$off("RealTimeClient.loggedOut");
    this.$rt.logout();
  },
};
</script>
<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
