<template>
  <div>
    <b-modal
      id="modal-5"
      title="Alert Filters"
      centered
      v-on:ok="doSaveFilter"
      v-on:cancel="doCancelFilter"
    >
      <b-input-group prepend="Alert Filter Type">
        <b-dropdown
          :text="filter.alertFilterType"
          :variant="filter.alertFilterType"
          size="sm"
          lazy
          no-flip
        >
          <b-dropdown-form>
            <b-button-toolbar>
              <b-button-group class="mx-1">
                <b-button
                  variant="Suppress"
                  v-on:click="
                    filter.alertFilterType = 'Suppress';
                    $el.ownerDocument.body.click();
                  "
                  >Suppress</b-button
                >
                <b-button
                  variant="Breach"
                  v-on:click="
                    filter.alertFilterType = 'Breach';
                    $el.ownerDocument.body.click();
                  "
                  >Breach</b-button
                >
              </b-button-group>
            </b-button-toolbar>
          </b-dropdown-form>
        </b-dropdown>
      </b-input-group>
      <b-input-group prepend="Interval">
        <b-input type="number" v-model="filter.expirySeconds"></b-input>
      </b-input-group>
      <b-input-group prepend="Count">
        <b-input v-model="filter.expiryThreshold"></b-input>
      </b-input-group>
    </b-modal>
  </div>
</template>

<script>
// @ is an alias to /src
import AlertFilter from "@/Utils/AlertFilter.js";
export default {
  name: "AlertFilterEditor",
  data: function () {
    return {
      filter: new AlertFilter(),
    };
  },
  watch: {
    currentFilter: {
      deep: true,
      handler() {
        this.filter = this.$attrs.value
          ? JSON.parse(JSON.stringify(this.$attrs.value))
          : new AlertFilter();
      },
    },
  },
  computed: {
    currentFilter() {
      return this.$attrs.value;
    },
  },
  methods: {
    doCancelFilter() {
      this.filter = JSON.parse(JSON.stringify(this.$attrs.value));
    },
    doSaveFilter() {
      this.$emit("input", Object.assign({}, this.filter));
    },
  },
};
</script>
