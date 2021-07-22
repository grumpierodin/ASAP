// eslint-disable-next-line no-unused-vars
import Uuid from "../Utils/Uuid.js";
export default class Schedule {
  constructor() {
    this.name = "Default";
    this.uuid = new Uuid().getCurrent();
    this.startTime = "00:00";
    this.endTime = "23:59";
    this.days = [
      "SUNDAY",
      "MONDAY",
      "TUESDAY",
      "WEDNESDAY",
      "THURSDAY",
      "FRIDAY",
      "SATURDAY",
    ];
  }
  getStartTime() {
    return this.startTime;
  }
  setStartTime(id) {
    this.startTime = id;
  }
  getEndTime() {
    return this.endTime;
  }
  setEndTime(id) {
    this.endTime = id;
  }
  getDays() {
    return this.days;
  }
  setDays(id) {
    this.days = id;
  }
  getUUID() {
    return this.uuid;
  }
  setUUID(id) {
    this.uuid = id;
  }
  getName() {
    return this.name;
  }
  setName(id) {
    this.name = id;
  }
}
