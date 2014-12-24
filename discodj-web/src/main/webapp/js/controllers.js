"use strict";

var discoDjControllers = angular.module('discoDjControllers', []);

discoDjControllers.controller("SearchCtrl", ["$scope", "$routeParams", "$route", "$location", "Search", "Playlist",
  function($scope, $routeParams, $route, $location, Search, Playlist) {
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

    $scope.addToPlaylist = function(mediaId) {
      Playlist.add({id: mediaId}, function(added) {
        $location.path("/playlist").search($routeParams);
      });
    }

    $scope.prevPage = function() {
      if ($scope.query.page || $scope.query.page > 0) {
        $scope.query.page = $scope.query.page - 1;
      } else {
        $scope.query.page = 1;
      }
      $route.updateParams($scope.query);
      $scope.results = Search.query($scope.query);
    }

    $scope.nextPage = function() {
      if ($scope.query.page) {
        $scope.query.page = $scope.query.page + 1;
      } else {
        $scope.query.page = 2;
      }
      $route.updateParams($scope.query);
      $scope.results = Search.query($scope.query);
    }
  }
]);

discoDjControllers.controller("PlaylistCtrl", ["$scope", "Playlist",
  function($scope, Playlist) {
    $scope.playlist = Playlist.query();
  }
]);
