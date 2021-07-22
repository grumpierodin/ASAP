<template>
  <div>
    <b-button v-b-modal.modal-1>Regex Tester</b-button>
    <b-modal id="modal-1" centered title="Regex Tester" v-on:ok="doSaveRegEx">
      <b-form-textarea
        id="textarea"
        v-model="text"
        placeholder="Enter something..."
        rows="3"
        max-rows="6"
      ></b-form-textarea>
      <b-form-textarea
        v-model="$attrs.value"
        placeholder="Enter regex..."
        rows="3"
        max-rows="6"
      ></b-form-textarea>
      <b-form-textarea
        v-model="currentTemplate"
        placeholder="awaiting render template..."
        rows="3"
        max-rows="6"
      ></b-form-textarea>
      <b-btn block v-on:click="doRegEx">Test RegEx</b-btn>
      <div style="height: 20vh; overflow-y: auto" v-if="preview">
        <b-list-group>
          <b-list-group-item v-for="item in fields" :key="item"
            >{{ item.name }} : {{ item.value }}</b-list-group-item
          >
        </b-list-group>
      </div>
      <b-form-textarea
        v-model="preview"
        placeholder="awaiting render test..."
        rows="3"
        max-rows="6"
      ></b-form-textarea>
    </b-modal>
  </div>
</template>

<script>
// @ is an alias to /src
import Matcher from "@/Utils/Matcher.js";

export default {
  name: "RegExEditor",
  props: {
    output: {
      type: String,
    },
  },
  data: function () {
    return {
      text: "this is a sample",
      template: this.output,
      preview: "",
      fields: [],
    };
  },
  computed: {
    currentTemplate: {
      get: function () {
        return this.output;
      },
      set: function (value) {
        this.template = value;
      },
    },
  },
  methods: {
    doRegEx() {
      let matcher = new Matcher(this.$attrs.value, this.text, this.template);
      this.preview = matcher.renderTemplate();
      this.fields = this.preview
        ? Object.entries(matcher.getMatchedParams()).map(([k, v]) => ({
            name: k,
            value: v,
          }))
        : [];
    },
    doSaveRegEx() {
      this.$emit("input", this.$attrs.value);
    },
  },
};
</script>
