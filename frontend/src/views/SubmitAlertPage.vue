<template>
  <div class="about">
    <h1>Submit Test Alert</h1>
    <b-form @submit.prevent="submitForm">
      <div style="width: 30vw; margin: auto">
        <b-card>
          <b-input-group prepend="Hostname">
            <b-input v-model="form.host" />
            <b-input-group prepend="Application">
              <b-input v-model="form.application" />
            </b-input-group>
          </b-input-group>
          <b-input-group prepend="Message">
            <b-input v-model="form.message" />
          </b-input-group>

          <b-button type="submit" v-on:click="submitForm($event)"
            >Submit</b-button
          >
          <b-alert
            @dismissed="dismisscountdown = 0"
            :variant="status"
            :show="dismisscountdown"
            >{{ errorMessage }}</b-alert
          >
        </b-card>
      </div>
    </b-form>
  </div>
</template>
<script>
import publishingService from "../services/publishingservice";

export default {
  name: "testmessage",
  data: function () {
    return {
      form: {
        message: "",
        host: "",
        application: "",
      },
      errorMessage: "",
      status: "",
      dismisscountdown: 0,
    };
  },
  methods: {
    submitForm(event) {
      event.preventDefault();
      event.stopPropagation();
      publishingService
        .send(this.form)
        .then(() => {
          this.status = "success";
          this.errorMessage = "Event Sent to Server";
          this.dismisscountdown = 3;
        })
        .catch((error) => {
          this.status = "danger";
          console.log(JSON.stringify(error));
          this.errorMessage = error.message
            ? error.message
            : JSON.stringify(error);
          this.dismisscountdown = 10;
        });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
