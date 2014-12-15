"use strict";

var discoDjControllers = angular.module('discoDjControllers', []);

discoDjControllers.controller("SearchCtrl", ["$scope", "$routeParams", "$route", "Search",
  function($scope, $routeParams, $route, Search) {
    $scope.query = $routeParams;
    $scope.results = Search.query($scope.query);

    $scope.search = function() {
      $scope.query.facet = [];
      $route.updateParams($scope.query);
      $scope.results = Search.query($scope.query);
    }

    $scope.facetSearch = function() {
      $route.updateParams($scope.query);
      $scope.results = Search.query($scope.query);
    }
  }
]);
