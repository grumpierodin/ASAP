// eslint-disable-next-line no-unused-vars
import Uuid from "@/Utils/Uuid.js";
import Schedule from "@/Utils/Schedule.js";
export default class rule {
  constructor() {
    this.name = "Change Me";
    this.uuid = new Uuid().getCurrent();
    this.correlationId = "{{uuid}}";
    this.regex = "(.+)";
    this.outputTemplate = "{{$0}}";
    this.alertLevel = "Low";
    this.schedules = [new Schedule()];
    this.alertFilters = [];
  }
  getRegEx() {
    return this.regex;
  }
  setRegEx(id) {
    this.regex = id;
  }
  getCorrelationId() {
    return this.correlationId;
  }
  setCorrelationId(id) {
    this.correlationId = id;
  }
  getUUID() {
    return this.uuid;
  }
  setUUID(id) {
    this.uuid = id;
  }
  getTemplate() {
    return this.outputTemplate;
  }
  setTemplate(id) {
    this.outputTemplate = id;
  }
  getName() {
    return this.name;
  }
  setName(id) {
    this.name = id;
  }
  getAlertFilters() {
    return this.alertFilters;
  }
  setAlertFilters(value) {
    this.alertFilters = value;
  }
}
