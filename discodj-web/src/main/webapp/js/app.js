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
        controller: "SearchCtrl"
      }).
      otherwise({
        redirectTo: "/search"
      }
    );
  }
]);
