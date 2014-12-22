"use strict";

var discoDjApp = angular.module("discoDjApp", [
  "ngRoute",
  "checklist-model",
  "discoDjControllers",
  "discoDjServices"
]);

discoDjApp.config(["$routeProvider",
  function($routeProvider) {
    $routeProvider.
      when("/search", {
        templateUrl: "partials/search.html",
        reloadOnSearch: false
      }).
      when("/playlist", {
        templateUrl: "partials/playlist.html",
      }).
      otherwise({
        redirectTo: "/search"
      }
    );
  }
]);
