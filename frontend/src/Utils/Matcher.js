// eslint-disable-next-line no-unused-vars
export default class Matcher {
  constructor(regexTextIn, message, template) {
    this.regexText = new RegExp(regexTextIn);
    this.match = this.regexText.exec(message);
    this.template = template;
    this.matchedFields = this.getMatchedParams();
  }
  isString(x) {
    return Object.prototype.toString.call(x) === "[object String]";
  }
  getMatchedParams() {
    let matched = {};
    if (this.match && this.match !== null) {
      Object.keys(this.match).forEach((o) => {
        let element = Number(o);
        if (this.isString(o) && isNaN(element)) {
          if (o === "groups" && this.match.groups) {
            Object.keys(this.match.groups).forEach((g) => {
              matched[g] = this.match.groups[g];
            });
          }
        } else {
          matched["$" + o] = this.match[o];
        }
      });
    }
    return Object.keys(matched).length > 0 ? matched : undefined;
  }
  addMatchedParam(param, value) {
    this.matchedFields[param] = value;
  }
  template() {
    return this.template;
  }
  setTemplate(template) {
    this.template = template;
  }
  renderTemplate() {
    let output = "";
    if (this.matchedFields && this.matchedFields !== null) {
      output = this.template;
      Object.keys(this.matchedFields).forEach((key) => {
        let searchKey = key.replace("$", "[$]");
        let regText = new RegExp("[{][{]+" + searchKey + "[}][}]+", "g");
        output = output.replace(regText, this.matchedFields[key]);
      });
    }
    return output;
  }
}
