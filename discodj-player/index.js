var minimist = require("minimist");
var discoDjPlayer = require("./src/main/js/discodj-player.js");

var options = minimist(process.argv.slice(2), {
  "string": ["servicesBaseUrl"],
  "default": {
    "servicesBaseUrl": "http://localhost:58008/discodj/resources"
  }
});

console.log("DiscoDJ Player starting up using " + options.servicesBaseUrl);
discoDjPlayer.start(options.servicesBaseUrl);
