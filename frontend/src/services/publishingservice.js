import axios from "axios";
import $store from "../store";
export default {
  /**
   * send a testevent toserver
   * @param {Object} event event detail
   */

  send(alert) {
    return new Promise((resolve, reject) => {
      axios
        .post("/event", alert, {
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
