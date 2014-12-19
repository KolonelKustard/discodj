"use strict";

var discoDjServices = angular.module("discoDjServices", ["ngResource"]);

discoDjServices.factory("Search", ["$resource",
  function($resource) {
    return $resource("resources/search", {}, {
      query: {
        method: "GET"
      }
    });
  }
]);

discoDjServices.factory("Playlist", ["$resource",
  function($resource) {
    return $resource("resources/playlist", {}, {
      query: {
        method: "GET"
      },
      add: {
        method: "PUT",
        url: "resources/playlist/add"
      }
    });
  }
]);
