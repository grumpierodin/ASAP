import axios from "axios";
import $store from "../store";
export default {
  /**
   * Authenticate a login request
   * @param {Object} detail login detail
   */

  getAlerts() {
    return new Promise((resolve, reject) => {
      axios
        .get("/alert", {
          headers: {
            Authorization: "Bearer " + $store.getters.getToken, //the token is a variable which holds the token
          },
        })
        .then(({ data }) => {
          resolve(data);
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
  updateAlert(alert) {
    return new Promise((resolve, reject) => {
      axios
        .post("/alert", alert, {
          headers: {
            Authorization: "Bearer " + $store.getters.getToken, //the token is a variable which holds the token
          },
        })
        .then(({ data }) => {
          resolve(data);
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
};
