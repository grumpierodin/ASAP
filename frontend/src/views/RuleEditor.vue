<template>
  <div class="home" v-if="rule">
    <b-input-group prepend="Rule Name">
      <b-form-input v-model="rule.name"></b-form-input>
    </b-input-group>
    <b-input-group prepend="RegEx">
      <b-form-input v-model="rule.regex"></b-form-input>
      <b-input-group-append>
        <regex-editor
          v-model="rule.regex"
          v-bind:output="currentTemplate"
        ></regex-editor>
      </b-input-group-append>
    </b-input-group>
    <b-input-group prepend="CorrelationId">
      <b-form-input v-model="rule.correlationId"></b-form-input>
    </b-input-group>
    <b-input-group prepend="Alert Template">
      <b-form-input v-model="rule.outputTemplate"></b-form-input>
    </b-input-group>
    &nbsp;
    <b-input-group prepend="Alert Level">
      <b-dropdown
        :text="rule.alertLevel"
        :variant="rule.alertLevel"
        size="sm"
        lazy
        no-flip
      >
        <b-dropdown-form>
          <b-button-toolbar>
            <b-button-group class="mx-1">
              <b-button
                variant="High"
                v-on:click="
                  rule.alertLevel = 'High';
                  $el.ownerDocument.body.click();
                "
                >High</b-button
              >
              <b-button
                variant="Medium"
                v-on:click="
                  rule.alertLevel = 'Medium';
                  $el.ownerDocument.body.click();
                "
                >Medium</b-button
              >
              <b-button
                variant="Low"
                v-on:click="
                  rule.alertLevel = 'Low';
                  $el.ownerDocument.body.click();
                "
                >Low</b-button
              >
            </b-button-group>
          </b-button-toolbar>
        </b-dropdown-form>
      </b-dropdown>
    </b-input-group>
    &nbsp;
    <b-list-group>
      <b-button v-on:click="addSchedule()">New Schedule</b-button>
      <div style="height: 20vh; overflow-y: auto" v-if="rule.schedules">
        <b-list-group-item
          v-for="(item, index) in rule.schedules"
          :key="item.uuid"
        >
          <b-button v-b-modal.modal-2 v-on:click="doScheduleEdit(index)"
            >Edit</b-button
          >
          <b-button v-on:click="downSchedule(index)"> - </b-button>
          <b-button v-on:click="upSchedule(index)"> + </b-button>
          <b-button v-on:click="delSchedule(index)">Del</b-button>&nbsp;
          <b-badge>{{ item.startTime }} {{ item.endTime }}</b-badge
          >&nbsp;
          <b-badge v-for="day in item.days" :key="day">{{
            day.substring(0, 3)
          }}</b-badge>
        </b-list-group-item>
      </div>
    </b-list-group>
    <schedule-editor
      v-if="rule.schedules"
      v-model="rule.schedules[selected]"
    ></schedule-editor>
    <b-list-group>
      <b-button v-on:click="addAlertFilter()">New Alert Filter</b-button>
      <div style="height: 20vh; overflow-y: auto" v-if="rule.alertFilters">
        <b-list-group-item
          v-for="(item, index) in rule.alertFilters"
          :key="item.filterId"
        >
          <b-button v-b-modal.modal-5 v-on:click="doAlertFilterEdit(index)"
            >Edit</b-button
          >
          <b-button v-on:click="downAlertFilter(index)"> - </b-button>
          <b-button v-on:click="upAlertFilter(index)"> + </b-button>
          <b-button v-on:click="delAlertFilter(index)">Del</b-button>&nbsp;
          <b-badge :variant="item.alertFilterType">{{
            item.alertFilterType
          }}</b-badge
          >&nbsp; <b-badge>Interval {{ item.expirySeconds }}</b-badge
          >&nbsp;
          <b-badge>Threshold {{ item.expiryThreshold }}</b-badge>
        </b-list-group-item>
      </div>
    </b-list-group>
    <alert-filter-editor
      v-if="rule.alertFilters"
      v-model="rule.alertFilters[selectedFilter]"
    ></alert-filter-editor>
    <b-button v-on:click="doCloseDialog">Cancel</b-button>
    <b-button v-on:click="doRuleApply">Apply</b-button>
  </div>
</template>

<script>
// @ is an alias to /src
import RegExEditor from "@/views/RegexEditor.vue";
import ScheduleEditor from "@/views/ScheduleEditor.vue";
import Uuid from "@/Utils/Uuid.js";
import Schedule from "@/Utils/Schedule.js";
import AlertFilter from "@/Utils/AlertFilter.js";
import Rule from "@/Utils/Rule.js";
import AlertFilterEditor from "@/views/AlertFilterEditor";

export default {
  name: "RuleEditor",
  components: {
    AlertFilterEditor,
    RegexEditor: RegExEditor,
    ScheduleEditor: ScheduleEditor,
  },
  data: function () {
    return {
      rule: new Rule(),
      selected: 0,
      selectedFilter: 0,
    };
  },
  watch: {
    currentRule: {
      deep: true,
      handler() {
        this.rule = this.$attrs.value
          ? JSON.parse(JSON.stringify(this.$attrs.value))
          : new Rule();
      },
    },
  },
  computed: {
    currentRule() {
      return this.$attrs.value ? this.$attrs.value : new Rule();
    },
    currentTemplate() {
      return this.rule.outputTemplate;
    },
  },
  methods: {
    doCloseDialog() {
      this.rule = JSON.parse(JSON.stringify(this.$attrs.value));
      this.$store.commit("ruleVisible", false);
    },
    doRuleApply() {
      this.$emit("input", this.rule);
      this.$store.commit("ruleVisible", false);
    },
    doAlertFilterEdit(index) {
      this.selectedFilter = index;
    },
    addAlertFilter() {
      let alertFilter = new AlertFilter();
      alertFilter.setFilterId(new Uuid().getCurrent());
      if (this.rule.alertFilters) {
        this.rule.alertFilters.splice(0, 0, alertFilter);
      } else {
        this.rules.alertFilters = [];
        this.rules.alertFilters.push(alertFilter);
      }
      this.selectedFilter = 0;
    },
    delAlertFilter(index) {
      this.rule.alertFilters.splice(index, 1);
      this.selectedFilter = index;
    },
    upAlertFilter(index) {
      let element = this.rule.alertFilters.splice(index, 1);
      this.rule.alertFilters.splice(
        index > 0 ? index - 1 : index,
        0,
        element[0]
      );
      this.selectedFilter = index;
    },
    downAlertFilter(index) {
      let element = this.rule.alertFilters.splice(index, 1);
      this.rule.alertFilters.splice(
        index < this.rule.alertFilters.length ? index + 1 : index,
        0,
        element[0]
      );
      this.selectedFilter = index;
    },
    doScheduleEdit(index) {
      this.selected = index;
    },
    addSchedule() {
      let schedule = new Schedule();
      schedule.setUUID(new Uuid().getCurrent());
      if (this.rule.schedules) {
        this.rule.schedules.splice(0, 0, schedule);
      } else {
        this.rules.schedules = [];
        this.rules.schedules.push(schedule);
      }
      this.selected = 0;
    },
    delSchedule(index) {
      this.rule.schedules.splice(index, 1);
      this.selected = index;
    },
    upSchedule(index) {
      let element = this.rule.schedules.splice(index, 1);
      this.rule.schedules.splice(index > 0 ? index - 1 : index, 0, element[0]);
      this.selected = index;
    },
    downSchedule(index) {
      let element = this.rule.schedules.splice(index, 1);
      this.rule.schedules.splice(
        index < this.rule.schedules.length ? index + 1 : index,
        0,
        element[0]
      );
      this.selected = index;
    },
  },
};
</script>
