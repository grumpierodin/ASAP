// eslint-disable-next-line no-unused-vars
import Uuid from "../Utils/Uuid.js";
export default class AlertFilter {
  constructor() {
    this.filterId = new Uuid().getCurrent();
    this.expirySeconds = 30;
    this.expiryThreshold = 6;
    this.alertFilterType = "Suppress";
  }
  getExpirySeconds() {
    return this.expirySeconds;
  }
  setExpirySeconds(id) {
    this.expirySeconds = id;
  }
  getExpiryThreshold() {
    return this.expiryThreshold;
  }
  setExpiryThreshold(id) {
    this.expiryThreshold = id;
  }
  getAlertFilterType() {
    return this.alertFilterType;
  }
  setAlertFilterType(id) {
    this.alertFilterType = id;
  }
  getFilterId() {
    return this.uuid;
  }
  setFilterId(id) {
    this.uuid = id;
  }
  getName() {
    return this.name;
  }
  setName(id) {
    this.name = id;
  }
}
