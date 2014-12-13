"use strict";

var discoDjServices = angular.module("discoDjServices", ["ngResource"]);
discoDjServices.factory("Search", ["$resource",
  function($resource){
    return $resource("resources/search", {}, {
      query: {
        method: "GET"
      }
    });
  }
]);
