<template>
  <div>
    <b-modal
      id="modal-2"
      title="Schedule"
      centered
      v-on:ok="doSaveSchedule"
      v-on:cancel="doCancelSchedule"
    >
      <b-form-group>
        <b-form-checkbox-group
          id="checkbox-group-1"
          v-model="schedule.days"
          :options="options"
        ></b-form-checkbox-group>
      </b-form-group>
      <b-input-group prepend="StartTime">
        <b-form-timepicker v-model="schedule.startTime"></b-form-timepicker>
      </b-input-group>
      <b-input-group prepend="EndTime">
        <b-form-timepicker v-model="schedule.endTime"></b-form-timepicker>
      </b-input-group>
    </b-modal>
  </div>
</template>

<script>
// @ is an alias to /src
import Schedule from "@/Utils/Schedule.js";
export default {
  name: "ScheduleEditor",
  data: function () {
    return {
      schedule: new Schedule(),
      options: [
        { text: "Sun", value: "SUNDAY" },
        { text: "Mon", value: "MONDAY" },
        { text: "Tue", value: "TUESDAY" },
        { text: "Wed", value: "WEDNESDAY" },
        { text: "Thu", value: "THURSDAY" },
        { text: "Fri", value: "FRIDAY" },
        { text: "Sat", value: "SATURDAY" },
      ],
    };
  },
  watch: {
    currentSchedule: {
      deep: true,
      handler() {
        this.schedule = this.$attrs.value
          ? JSON.parse(JSON.stringify(this.$attrs.value))
          : new Schedule();
      },
    },
  },
  computed: {
    currentSchedule() {
      return this.$attrs.value;
    },
  },
  methods: {
    doCancelSchedule() {
      this.schedule = JSON.parse(JSON.stringify(this.$attrs.value));
    },
    doSaveSchedule() {
      this.$emit("input", Object.assign({}, this.schedule));
    },
  },
};
</script>
