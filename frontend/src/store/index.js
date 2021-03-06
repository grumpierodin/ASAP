import Vue from "vue";
import Vuex from "vuex";
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    regExText: ".+",
    rule: {},
    schedule: {},
    ruleVisible: false,
    alerts: [],
    realTimeMessage: [],
    token: "",
    realtimeServer: "",
    deDup: "true",
    alertFilters: [],
    alertTextFilters: "",
    alertStates: [],
    alertLevels: [],
    validStates: ['Open', 'Resolved'],
    validLevels: ['High', 'Medium', 'Low'],
  },
  mutations: {
    setDeDup(state, dedup) {
      state.deDup = dedup;
    },
    setAlertTextFilters(state, filters) {
      state.alertTextFilters = filters;
    },
    setAlertFilters(state, filters) {
      state.alertFilters = filters;
    },
    setAlertStates(state, filters) {
      state.alertStates = filters;
    },
    setAlertLevels(state, filters) {
      state.alertLevels = filters;
    },
    setAlerts(state, alerts) {
      state.alerts = alerts;
    },
    setToken(state, token) {
      state.token = token;
    },
    setRealtimeServer(state, url) {
      state.realtimeServer = url;
    },
    setRealtimeMessage(state, message) {
      state.realTimeMessage.splice(
        0,
        0,
        new Date().toISOString() + " | " + message
      );
      if (state.realTimeMessage.length > 4) {
        state.realTimeMessage.splice(3, 1);
      }
    },
    setRegExText(state, text) {
      state.regExText = text;
    },
    setRule(state, rule) {
      state.rule = Object.assign({}, rule);
    },
    setSchedule(state, schedule) {
      state.schedule = Object.assign({}, schedule);
    },
    ruleVisible(state, visible) {
      state.ruleVisible = visible;
    },
  },
  getters: {
    getAlertTextFilters: (state) => {
      return state.alertTextFilters;
    },
    getAlertFilters: (state) => {
      return state.alertFilters;
    },
    getAlertLevels: (state) => {
      return state.alertLevels;
    },
    getAlertStates: (state) => {
      return state.alertStates;
    },
    getValidStates: (state) => {
      return state.validStates;
    },
    getValidLevels: (state) => {
      return state.validLevels;
    },
    getDeDup: (state) => {
      return state.deDup;
    },
    getRealtimeServer: (state) => {
      return state.realtimeServer;
    },
    getToken: (state) => {
      return state.token;
    },
    getRegExText: (state) => {
      return state.regExText;
    },
    getRealtimeMessage: (state) => {
      return state.realTimeMessage;
    },
    getAlerts: (state) => {
      return state.alerts;
    },
    getRule: (state) => {
      return state.rule ? state.rule : {};
    },
    getSchedule: (state) => {
      return state.schedule ? state.schedule : {};
    },
    getRuleVisible: (state) => {
      return state.ruleVisible;
    },
  },
  actions: {},
  modules: {},
});
