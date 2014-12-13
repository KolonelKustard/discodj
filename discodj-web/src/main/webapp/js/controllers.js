"use strict";

var discoDjControllers = angular.module('discoDjControllers', []);

discoDjControllers.controller("SearchCtrl", ["$scope", "Search",
  function($scope, Search) {
    $scope.results = Search.query();
  }
]);
