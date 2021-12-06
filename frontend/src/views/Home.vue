<template>
  <div class="home">
    <div style="width: 80vw; margin: auto">
      <b-card title="Alerts" style="height: 80vh; max-height: 85vh">
        <b-form-radio-group
          size="sm"
          buttons
          v-on:change="$store.commit('setDeDup', $event)"
        >
          <b-form-radio
            :button-variant="
              this.$store.getters.getDeDup !== 'true' ? 'secondary' : 'Low'
            "
            value="true"
            >Summary</b-form-radio
          >

          <b-form-radio
            :button-variant="
              this.$store.getters.getDeDup !== 'false' ? 'secondary' : 'Low'
            "
            value="false"
            >Event List</b-form-radio
          >
          <b-form-radio
            v-if="this.$store.getters.getAlertFilters.length > 0"
            :button-variant="
              this.$store.getters.getDeDup !== 'filtered' ? 'secondary' : 'Low'
            "
            value="filtered"
            >Filter Id's</b-form-radio
          >
        </b-form-radio-group>
        <b-table
          small
          striped
          outlined
          :filter="$store.getters.getAlertTextFilters"
          :filter-included-fields="['alertText']"
          show-empty
          sticky-header="90%"
          :no-border-collapse="false"
          responsive
          primary-key="uuid"
          :items="currentAlerts"
          :fields="currentFields"
          :tbody-transition-props="transProps"
        >
          <template #head(id)="scope">
            <div class="text-nowrap">Row ID</div>
            {{ scope.id }}
          </template>
          <template #head()="scope">
            <div
              class="text-nowrap"
              style="vertical-align: center; height: 20px;"
            >
              <span v-if="scope.label != 'Alert Text' && scope.label != 'Alert State'">{{ scope.label }}</span>
              <div style="float: right" v-if="scope.label === 'Alert Text'">
                <span v-if="!editFilter">{{ scope.label }}</span>
                <span v-if="editFilter">
                  <b-input-group prepend="Filter Text">
                    <b-input
                      :value="$store.getters.getAlertTextFilters"
                      v-on:change="
                        $store.commit('setAlertTextFilters', $event);
                        editFilter = false;
                      "
                    ></b-input>
                    <b-button
                      v-if="editFilter"
                      variant="light"
                      size="sm"
                      @click="
                        $store.commit('setAlertTextFilters', '');
                        editFilter = false;
                      "
                      >X</b-button
                    >
                    <b-button
                      v-if="editFilter"
                      variant="light"
                      size="sm"
                      @click="editFilter = !editFilter"
                    >
                      <b-icon
                        :variant="
                          $store.getters.getAlertTextFilters.length > 0
                            ? 'Open'
                            : 'Resolved'
                        "
                        icon="filter"
                      ></b-icon>
                    </b-button>
                  </b-input-group>
                </span>
                <b-button
                  v-if="!editFilter"
                  variant="light"
                  size="sm"
                  @click="editFilter = !editFilter"
                >
                  <b-icon
                    :variant="
                      $store.getters.getAlertTextFilters.length > 0
                        ? 'Open'
                        : 'Resolved'
                    "
                    icon="filter"
                  ></b-icon>
                </b-button>
              </div>
              <div style="float: right;" v-if="scope.label === 'Alert State'">
                <span>
                  {{ scope.label }}
                    <b-badge :variant="field" v-for="field in $store.getters.getAlertStates"
                             :key="field">{{
                        field
                      }}<b-button
                        variant="light"
                        size="sm"
                        @click="
                        //$el.ownerDocument.defaultView.console.log($event);
                        const text = $event.target.parentElement.firstChild.nodeValue;
                        const index = $store.getters.getAlertStates.indexOf(text);
                        const alertStates = $store.getters.getAlertStates;
                        alertStates.splice(index,1);
                        if(index >=0) {
                          $store.commit('setAlertStates', alertStates.sort())
                        }
                        $el.ownerDocument.body.click();
                    "
                    >X</b-button
                    ></b-badge>
                    <b-button
                        v-if="$store.getters.getAlertStates.length > 0"
                        variant="light"
                        size="sm"
                        @click="
                        $store.commit('setAlertStates', []);
                      "
                    >X</b-button
                    >
                  <b-dropdown dropleft text="+" no-caret variant="light">
                    <b-dropdown-form>
                      <b-button-toolbar>
                        <b-button-group class="mx-1">
                          <b-button :variant="field" v-for="field in ['Open', 'Resolved']"
                                    :key="field"
                              @click="
                              //$el.ownerDocument.defaultView.console.log($event);
                              const text = $event.target.innerText;
                              const index = $store.getters.getAlertStates.indexOf(text);
                              const alertStates = $store.getters.getAlertStates;
                              index >=0 ? alertStates.splice(index,1) : alertStates.push(text);
                              $store.commit('setAlertStates', alertStates.sort());
                              $el.ownerDocument.body.click();
                          "
                          >{{ field }}</b-button
                          >
                        </b-button-group>
                      </b-button-toolbar>
                    </b-dropdown-form>
                    </b-dropdown>
                </span>
              </div>
            </div>
          </template>
          <template #cell(count)="data">
            <b>({{ data.item.count }})</b>
          </template>

          <template #cell(alertCorrelationId)="data">
            {{ data.item.alertCorrelationId }}
            <div style="float: right">
              <b-button
                :title="
                  $store.getters.getAlertFilters.indexOf(
                    data.item.alertCorrelationId
                  ) >= 0
                    ? 'Remove from filter list'
                    : 'Add to filter list'
                "
                variant="light"
                size="sm"
                v-on:click="doFiltering(data.item)"
                ><b-icon
                  :variant="
                    $store.getters.getAlertFilters.indexOf(
                      data.item.alertCorrelationId
                    ) >= 0
                      ? 'Open'
                      : 'Resolved'
                  "
                  icon="filter"
                ></b-icon
              ></b-button>
            </div>
          </template>
          <template #cell(alertLevel)="data">
            <b-dropdown
              :text="data.item.alertLevel"
              :variant="data.item.alertLevel"
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
                        notifyAlertChange(data.item, 'alertLevel', 'High');
                        $el.ownerDocument.body.click();
                      "
                      >High</b-button
                    >
                    <b-button
                      variant="Medium"
                      v-on:click="
                        notifyAlertChange(data.item, 'alertLevel', 'Medium');
                        $el.ownerDocument.body.click();
                      "
                      >Medium</b-button
                    >
                    <b-button
                      variant="Low"
                      v-on:click="
                        notifyAlertChange(data.item, 'alertLevel', 'Low');
                        $el.ownerDocument.body.click();
                      "
                      >Low</b-button
                    >
                  </b-button-group>
                </b-button-toolbar>
              </b-dropdown-form>
            </b-dropdown>
          </template>
          <template #cell(alertState)="data">
            <b-dropdown
              :text="data.value"
              :variant="data.value"
              size="sm"
              lazy
              no-flip
              left
            >
              <b-dropdown-form>
                <b-button-toolbar>
                  <b-button-group class="mx-1">
                    <b-button
                      variant="Open"
                      v-on:click="
                        notifyAlertChange(data.item, 'alertState', 'Open');
                        $el.ownerDocument.body.click();
                      "
                      >Open</b-button
                    >
                    <b-button
                      variant="Resolved"
                      v-on:click="
                        notifyAlertChange(data.item, 'alertState', 'Resolved');
                        $el.ownerDocument.body.click();
                      "
                      >Resolved</b-button
                    >
                  </b-button-group>
                </b-button-toolbar>
              </b-dropdown-form>
            </b-dropdown>
          </template>
          <template #cell(actions)="row">
            <b-button
              variant="light"
              size="sm"
              @click="row.toggleDetails"
              title="hide/show details for this alert"
            >
              {{ row.detailsShowing ? "Hide" : "Show" }} Details
            </b-button>
          </template>
          <template #row-details="row">
            <b-card :title="row.item.alertText">
              <b-card title="Event Text" bg-variant="EventCard">
                {{ row.item.event.message }}
              </b-card>
              <b-card title="Rule Matched" bg-variant="DetailCard">
                <b-container>
                  <b-row>
                    <b-col><h2>Name</h2></b-col><b-col><h2>Regex</h2></b-col
                    ><b-col><h2>Correlation Id Definition</h2></b-col>
                  </b-row>
                  <b-row>
                    <b-col>{{ row.item.rule.name }}</b-col
                    ><b-col>{{ row.item.rule.regex }}</b-col
                    ><b-col>{{ row.item.rule.correlationId }}</b-col>
                  </b-row>
                </b-container>
                &nbsp;
                <b-container>
                  <b-row>
                    <b-col>
                      <h2>Matched Fields</h2>
                      <b-container
                        v-for="field in Object.keys(row.item.matchedFields)"
                        :key="field"
                      >
                        <b-badge>
                          {{ field }} :
                          {{ row.item.matchedFields[field] }}</b-badge
                        >
                      </b-container>
                    </b-col>
                    <b-col>
                      <h2>Schedules</h2>
                      <b-container
                        v-for="schedule in row.item.rule.schedules"
                        :key="schedule.uuid"
                      >
                        <b-badge
                          >{{ schedule.startTime }}
                          {{ schedule.endTime }}</b-badge
                        >&nbsp;
                        <b-badge v-for="day in schedule.days" :key="day">{{
                          day.substring(0, 3)
                        }}</b-badge>
                      </b-container>
                    </b-col>
                    <b-col>
                      <h2>Alert Filters</h2>
                      <b-container
                        v-for="filter in row.item.rule.alertFilters"
                        :key="filter.filterId"
                      >
                        <b-badge :variant="filter.alertFilterType">{{
                          filter.alertFilterType
                        }}</b-badge
                        >&nbsp;
                        <b-badge>Interval {{ filter.expirySeconds }}</b-badge
                        >&nbsp;
                        <b-badge
                          >Threshold {{ filter.expiryThreshold }}</b-badge
                        >
                      </b-container>
                    </b-col>
                  </b-row>
                </b-container>
              </b-card>
              <br />
              <b-button size="sm" @click="row.toggleDetails()"
                >Hide Details</b-button
              >
            </b-card>
          </template>
        </b-table>
      </b-card>
      <b-alert :show="$store.getters.getRealtimeMessage.length > 0"
        >{{ $store.getters.getRealtimeMessage[0] }}
      </b-alert>
    </div>
  </div>
</template>

<script>
import alertService from "../services/alertservice";
export default {
  name: "Home",
  created() {
    this.$bus.$off("RealTimeClient.loggedOut");
    this.$bus.$off("RealTimeClient.reconnecting");
    this.unsubscribeFromRealTimeUpdate();
    this.loadAlerts();
    this.$bus.$on("RealTimeClient.reconnecting", () => {
      this.loadAlerts();
    });
    this.$bus.$on("RealTimeClient.loggedOut", () => {
      this.$router.push({ name: "login" });
    });
  },
  beforeMount() {
    window.addEventListener("beforeunload", this.preventNav);
  },
  beforeDestroy() {
    window.removeEventListener("beforeunload", this.preventNav);
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.loadAlerts();
    });
  },
  beforeRouteUpdate(to, from, next) {
    next();
    this.unsubscribeFromRealTimeUpdate();
    this.loadAlerts();
  },
  beforeRouteLeave(to, from, next) {
    next();
    this.unsubscribeFromRealTimeUpdate();
  },
  destroyed() {
    this.$bus.$off("RealTimeClient.loggedOut");
    this.$bus.$off("RealTimeClient.reconnecting");
    //this.$rt.logout();
  },
  computed: {
    currentAlerts: function () {
      const alertFilters = this.$store.getters.getAlertFilters;
      const alertStates = this.$store.getters.getAlertStates;
      const alertFiltered = this.$store.getters.getDeDup;
      return this.$store.getters.getDeDup === "false"
        ? this.alerts.filter(function (item) {
            return (
                (alertFilters.length <= 0 ||
              alertFilters.indexOf(item.alertCorrelationId) >= 0) &&
                (alertStates.length <= 0 ||alertStates.indexOf(item.alertState)>=0)
            );
          })
        : alertFiltered === "filtered" || alertStates.length >=0
        ? this.visibleAlerts.filter(function (item) {
            return (
                (alertFilters.length <= 0 ||
              alertFilters.indexOf(item.alertCorrelationId) >= 0) &&
                (alertStates.length <= 0 ||alertStates.indexOf(item.alertState)>=0)
            );
          })
        : this.visibleAlerts;
    },
    currentFields: function () {
      return this.$store.getters.getDeDup === "false"
        ? this.fields
        : this.deDupFields;
    },
    visibleAlerts: function () {
      return this.uniqueById(this.alerts, "alertCorrelationId");
    },
  },
  methods: {
    preventNav() {
      this.$rt.logout();
    },
    uniqueById(arr, prop) {
      return Object.values(
        arr.reduce(function (acc, item) {
          var entity =
            item && item[prop] && item[prop] === item.rule.uuid
              ? item[prop]
              : item[prop] + item.rule.uuid;
          if (item && item[prop] && !acc[entity]) {
            acc[entity] = item;
            acc[entity].count = 1;
          } else if (item && item[prop] && acc[entity]) {
            acc[entity].count += 1;
          }
          return acc;
        }, {})
      );
    },
    doFiltering(item) {
      const data = this.$store.getters.getAlertFilters;
      const index = data.indexOf(item.alertCorrelationId);
      index >= 0 ? data.splice(index, 1) : data.push(item.alertCorrelationId);
      this.$store.commit("setAlertFilters", data);
    },
    loadAlerts() {
      this.$store.commit("setRealtimeMessage", "[AlertsPage] Loading Alerts");
      alertService
        .getAlerts()
        .then((data) => {
          if (Array.isArray(data)) {
            this.alerts = data ? data : [];
            this.subscribeToRealTimUpdate();
          } else {
            this.$router.push({ name: "login" });
          }
        })
        .catch((error) => {
          this.$store.commit("setRealtimeMessage", error.message);
        });
    },
    subscribeToRealTimUpdate() {
      this.$rt.subscribe("/api/realtime/alert", this.onRealTimeUpdated);
    },
    unsubscribeFromRealTimeUpdate() {
      this.$rt.unsubscribe("/api/realtime/alert", this.onRealTimeUpdated);
    },
    onRealTimeUpdated(update) {
      this.$store.commit(
        "setRealtimeMessage",
        "[AlertsPage] Real time update received"
      );
      if (update) {
        var command = JSON.parse(update);
        if (command.type === "alertAdded" || command.type === "alertUpdate") {
          this.onAlertAdded(command.alert);
        }
        if (
          command.type === "failure" &&
          command.message.indexOf("authentication failed") >= 0
        ) {
          // need to reset realtimetoken
          if (this.$rt._isConnected()) {
            this.$rt.logout();
          }
        }
      }
    },
    onAlertAdded(alert) {
      var found = 0;
      for (var tableAlert in this.alerts) {
        if (this.alerts[tableAlert].uuid === alert.uuid) {
          this.alerts[tableAlert] = alert;
          this.alerts = Object.assign([], this.alerts);
          found = 1;
        }
      }
      if (found == 0) {
        this.alerts.splice(0, 0, alert);
      }
    },
    toggleRowDetails(row) {
      row.toggleDetails();
    },
    notifyAlertChange(alert, field, value) {
      const alertUpdate = JSON.parse(JSON.stringify(alert));
      if (field === "alertState") {
        alertUpdate.alertState = value;
        alertService.updateAlert(alertUpdate);
      } else {
        alertUpdate.alertLevel = value;
        alertService.updateAlert(alertUpdate);
      }
    },
  },
  data() {
    return {
      transProps: {
        // Transition name
        name: "alert-table",
      },
      deDupFields: [
        {
          key: "uuid",
          label: "Alert Id",
          stickyColumn: true,
          isRowHeader: true,
        },
        {
          key: "event.application",
          label: "Application",
        },
        {
          key: "event.host",
          label: "Host",
        },
        "alertText",
        "alertLevel",
        "alertState",
        "alertCorrelationId",
        {
          key: "count",
          label: "Event Count",
        },
        {
          key: "event.timestamp",
          label: "Event Time",
        },
        { key: "actions", label: "Actions" },
      ],
      fields: [
        {
          key: "uuid",
          label: "Alert Id",
          stickyColumn: true,
          isRowHeader: true,
        },
        {
          key: "event.application",
          label: "Application",
        },
        {
          key: "event.host",
          label: "Host",
        },
        "alertText",
        "alertLevel",
        "alertState",
        "alertCorrelationId",
        {
          key: "event.timestamp",
          label: "Event Time",
        },
        { key: "actions", label: "Actions" },
      ],
      alerts: [],
      editFilter: false,
      stateFilter: false,
    };
  },
};
</script>
<style scoped>
.alert-table-item {
  transition: all 1s;
}
.alert-table-item > * {
  transition: all 1s;
  overflow: hidden;
}
.alert-table-enter,
.alert-table-leave-to {
  line-height: 0;
}
.alert-table-enter > *,
.alert-table-leave-to > * {
  padding-top: 0px !important;
  padding-bottom: 0px !important;
}
</style>
