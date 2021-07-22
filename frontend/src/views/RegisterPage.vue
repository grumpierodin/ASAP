<template>
  <div class="about">
    <h1>Register</h1>
    <b-form @submit.passive="submitForm"> </b-form>
    <div style="width: 30vw; margin: auto">
      <b-card>
        <b-input-group prepend="Username">
          <b-input v-model="form.username" />
        </b-input-group>
        <b-input-group prepend="Password">
          <b-input type="password" v-model="form.password" />
        </b-input-group>
        <b-input-group prepend="Confirm Password">
          <b-input type="password" v-model="confirmPassword" />
        </b-input-group>
        <b-button
          type="submit"
          :disabled="checkPassword()"
          v-on:click="submitForm($event)"
          >Submit</b-button
        >
        <router-link to="/login">Login</router-link>
        <b-alert variant="danger" :show="errorMessage.length > 0">{{
          errorMessage
        }}</b-alert>
      </b-card>
    </div>
  </div>
</template>
<script>
import registrationService from "../services/authentication";
export default {
  name: "register",
  data: function () {
    return {
      form: {
        username: "",
        emailAddress: "",
        firstName: "",
        lastName: "",
        password: "",
      },
      confirmPassword: "",
      errorMessage: "",
    };
  },
  methods: {
    checkPassword() {
      if (
        this.form.password.length < 2 ||
        (this.form.password.length > 1 &&
          this.confirmPassword !== this.form.password)
      ) {
        return true;
      }
      return false;
    },
    submitForm(event) {
      event.preventDefault();
      event.stopPropagation();
      console.log("register...." + this.form);
      registrationService
        .register(this.form)
        .then(() => {
          this.$router.push({ name: "login" });
        })
        .catch((error) => {
          console.log(error);
          this.errorMessage = "Registration Failed ...registered already?";
        });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
