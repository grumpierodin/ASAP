import axios from "axios";
import eventBus from "@/event-bus";
import store from "../store";
export default {
  /**
   * Authenticate a login request
   * @param {Object} detail login detail
   */
  authenticate(detail) {
    return new Promise((resolve, reject) => {
      axios
        .post("/users/signin", detail)
        .then(({ data }) => {
          store.commit("setToken", data);
          resolve(data);
          eventBus.$emit("authenticated", data);
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
  realTimeServer() {
    return new Promise((resolve, reject) => {
      axios
        .get("/realtime/service", {
          headers: {
            Authorization: "Bearer " + this.store.getters.getToken, //the token is a variable which holds the token
          },
        })
        .then(({ data }) => {
          console.log(data);
          store.commit("setRealtimeServer", JSON.parse(data).realTimeServerUrl);
          resolve(data);
        })
        .catch((error) => {
          console.log(error);
          reject(error);
        });
    });
  },
  /**
   * Register a new user
   * @param {Object} detail registration detail
   */
  register(detail) {
    return new Promise((resolve, reject) => {
      axios
        .post("/users/signup", detail)
        .then(({ data }) => {
          resolve(data);
        })
        .catch((error) => {
          console.log(error);
          reject(error);
        });
    });
  },
};
