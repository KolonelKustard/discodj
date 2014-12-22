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
        $location.path("/playlist");
      });
    }
  }
]);

discoDjControllers.controller("PlaylistCtrl", ["$scope", "Playlist",
  function($scope, Playlist) {
    $scope.playlist = Playlist.query();
  }
]);
