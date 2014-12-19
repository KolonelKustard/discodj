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
      when("/dj-deck", {
        templateUrl: "partials/dj-deck.html",
        controller: "SearchCtrl",
        reloadOnSearch: false
      }).
      otherwise({
        redirectTo: "/dj-deck"
      }
    );
  }
]);
