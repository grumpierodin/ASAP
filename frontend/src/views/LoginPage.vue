<template>
  <div class="about">
    <h1>Login</h1>
    <b-form @submit.prevent="submitForm">
      <div style="width: 30vw; margin: auto">
        <b-card>
          <b-input-group prepend="Username">
            <b-input v-model="form.username" />
          </b-input-group>
          <b-input-group prepend="Password">
            <b-input type="password" v-model="form.password" />
          </b-input-group>
          <b-button type="submit" v-on:click="submitForm($event)"
            >Submit</b-button
          >
          |
          <router-link to="/register">Register</router-link>
          <b-alert variant="danger" :show="errorMessage.length > 0">{{
            errorMessage
          }}</b-alert>
        </b-card>
      </div>
    </b-form>
  </div>
</template>
<script>
import authenticationService from "../services/authentication";

export default {
  name: "login",
  data: function () {
    return {
      form: {
        username: "",
        password: "",
      },
      errorMessage: "",
      errors: [],
    };
  },
  methods: {
    submitForm(event) {
      event.preventDefault();
      event.stopPropagation();
      authenticationService
        .authenticate(this.form)
        .then((data) => {
          console.log(data);
        })
        .then(() => {
          console.log("Initializing the real time connection");
          this.$rt.init("/api/realtime/alert");
          this.$nextTick(function () {
            this.$router.push({ name: "Home" });
          });
        })
        .catch((error) => {
          console.log(JSON.stringify(error));
          this.errorMessage = "Login Failed ...registered?";
        });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
