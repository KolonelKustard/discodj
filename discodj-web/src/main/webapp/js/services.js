"use strict";

require("angular");
require("angular-resource");
var settings = require("./settings.js");

var discoDjServices = angular.module("discoDjServices", ["ngResource"]);

discoDjServices.factory("Search", ["$resource",
  function($resource) {
    return $resource(settings.serviceBaseUrl + "api/search", {}, {
      query: {
        method: "GET"
      }
    });
  }
]);

discoDjServices.factory("Playlist", ["$resource",
  function($resource) {
    return $resource(settings.serviceBaseUrl + "api/playlist", {}, {
      query: {
        method: "GET"
      },
      add: {
        method: "GET",
        url: settings.serviceBaseUrl + "api/playlist/add"
      },
      moveUp: {
        method: "GET",
        url: settings.serviceBaseUrl + "api/playlist/moveUp"
      },
      moveDown: {
        method: "GET",
        url: settings.serviceBaseUrl + "api/playlist/moveDown"
      },
      next: {
        method: "GET",
        url: settings.serviceBaseUrl + "api/playlist/next"
      }
    });
  }
]);
