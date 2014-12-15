"use strict";

var discoDjControllers = angular.module('discoDjControllers', []);

discoDjControllers.controller("SearchCtrl", ["$scope", "$routeParams", "$route", "Search",
  function($scope, $routeParams, $route, Search) {
    $scope.query = $routeParams;
    $scope.results = Search.query();

    $scope.search = function() {
      $route.updateParams($scope.query);
    }
  }
]);
