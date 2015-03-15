"use strict";

require("angular");
require("angular-resource");
var settings = require("./settings.js");

var discoDjServices = angular.module("discoDjServices", ["ngResource"]);

discoDjServices.factory("Search", ["$resource",
  function($resource) {
    return $resource(settings.serviceBaseUrl + "search", {}, {
      query: {
        method: "GET"
      }
    });
  }
]);

discoDjServices.factory("Playlist", ["$resource",
  function($resource) {
    return $resource(settings.serviceBaseUrl + "playlist", {}, {
      query: {
        method: "GET"
      },
      add: {
        method: "GET",
        url: settings.serviceBaseUrl + "playlist/add"
      },
      moveUp: {
        method: "GET",
        url: settings.serviceBaseUrl + "playlist/moveUp"
      },
      moveDown: {
        method: "GET",
        url: settings.serviceBaseUrl + "playlist/moveDown"
      },
      next: {
        method: "GET",
        url: settings.serviceBaseUrl + "playlist/next"
      }
    });
  }
]);
