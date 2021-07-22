<template>
  <div class="about">
    <h1>This is the rules page</h1>
    <b-container>
      <b-row>
        <b-col cols="6">
          <b-list-group>
            <b-button v-on:click="addRule()">New Rule</b-button>
            <div
              style="max-height: 50vh; overflow-y: auto"
              v-if="rules && rules.length > 0"
            >
              <b-list-group-item
                v-for="(item, index) in getRules"
                :key="item.uuid"
              >
                <b-button v-on:click="doRuleEdit(index)">Edit</b-button>
                <b-button v-on:click="downRule(index)"> - </b-button>
                <b-button v-on:click="upRule(index)"> + </b-button>
                <b-button v-on:click="delRule(index)">Del</b-button>
                {{ item.name }}
              </b-list-group-item>
            </div>
            &nbsp;
            <b-button title="Save to Server" v-on:click="sendRulesToServer()"
              >Save Rules</b-button
            >
            &nbsp;
            <b-button title="Download" v-on:click="exportRulesAsJson()"
              >Export Rules</b-button
            >
            &nbsp;
            <b-alert
              @dismissed="dismisscountdown = 0"
              :variant="status"
              :show="dismisscountdown"
              >{{ errorMessage }}</b-alert
            >
          </b-list-group>
        </b-col>
        <b-col>
          <b-overlay :show="getEditEnabled" opacity="0.45" spinner-type="none">
            <rule-editor v-model="rules[selected]"></rule-editor>
          </b-overlay>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>
<script>
// @ is an alias to /src
import RuleEditor from "@/views/RuleEditor.vue";
import Rule from "@/Utils/Rule.js";
import Uuid from "@/Utils/Uuid.js";
import axios from "axios";

export default {
  name: "Rules",
  components: {
    RuleEditor: RuleEditor,
  },
  data: function () {
    return {
      rules: [],
      selected: 0,
      errorMessage: "",
      status: "",
      dismisscountdown: 0,
    };
  },
  mounted: function () {
    this.getRulesFromServer();
  },
  computed: {
    currentRule() {
      return this.rules;
    },
    currentTemplate() {
      return this.rule.outputTemplate;
    },
    getRules() {
      return this.rules;
    },
    getEditEnabled() {
      return !this.$store.state.ruleVisible;
    },
  },
  methods: {
    exportRulesAsJson() {
      const blob = new Blob([JSON.stringify(this.rules, null, 4)], {
        type: "application/text",
      });
      const blobUrl = URL.createObjectURL(blob);
      var element = document.createElement("a");
      element.setAttribute("download", "rules.json");
      element.href = blobUrl;
      console.log(element);
      element.click();
      element.remove();
    },
    doCloseDialog() {},
    doRuleEdit(index) {
      this.selected = index;
      this.$store.commit("ruleVisible", true);
      this.$store.commit("setRule", this.rules[index]);
    },
    addRule() {
      let rule = new Rule();
      rule.setUUID(new Uuid().getCurrent());
      this.rules.splice(0, 0, rule);
      this.selected = 0;
    },
    delRule(index) {
      this.rules.splice(index, 1);
      this.selected = index;
      if (this.rules.length <= 0) {
        this.$store.commit("ruleVisible", false);
      }
    },
    upRule(index) {
      let element = this.rules.splice(index, 1);
      this.rules.splice(index > 0 ? index - 1 : index, 0, element[0]);
      this.selected = index;
    },
    downRule(index) {
      let element = this.rules.splice(index, 1);
      this.rules.splice(
        index < this.rules.length ? index + 1 : index,
        0,
        element[0]
      );
      this.selected = index;
    },
    async getRulesFromServer() {
      const data = await axios.get(`/rules`, {
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken, //the token is a variable which holds the token
        },
      });
      this.rules = data.data;
      this.selected = 0;
      this.$store.commit("setRule", this.rules[this.selected]);
      this.$store.commit("ruleVisible", false);
    },
    sendRulesToServer() {
      axios
        .post(`/rules`, this.rules, {
          headers: {
            Authorization: "Bearer " + this.$store.getters.getToken, //the token is a variable which holds the token
          },
        })
        .then((response) => {
          // JSON responses are automatically parsed.
          console.log(response);
          this.status = "success";
          this.errorMessage = "Saved to Server";
          this.dismisscountdown = 5;
        })
        .catch((error) => {
          console.log(error);
          this.status = "danger";
          this.errorMessage = "Failed to Update Server";
          this.dismisscountdown = 5;
        });
    },
  },
};
</script>
