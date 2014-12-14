"use strict";

var discoDjControllers = angular.module('discoDjControllers', []);

discoDjControllers.controller("SearchCtrl", ["$scope", "Search",
  function($scope, Search) {
    $scope.query = {
      q: "",
      facet: []
    };
    $scope.results = Search.query();
  }
]);
