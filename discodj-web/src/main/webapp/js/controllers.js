"use strict";

require("angular");
require("./services.js");

var discoDjControllers = angular.module('discoDjControllers', []);

discoDjControllers.controller("SearchCtrl", ["$scope", "$routeParams", "$route", "$location", "Search", "Playlist",
  function($scope, $routeParams, $route, $location, Search, Playlist) {
    $scope.query = $routeParams;
    $scope.results = Search.query($scope.query);

    $scope.gotoPlaylist = function() {
      $location.path("/playlist").search($routeParams);
    }

    $scope.search = function() {
      $scope.query.facet = [];
      $route.updateParams($scope.query);
      $scope.results = Search.query($scope.query);
    }

    $scope.facetSearch = function() {
      $route.updateParams($scope.query);
      $scope.results = Search.query($scope.query);
    }

    $scope.clearSearch = function() {
      $scope.query = {};
      $route.updateParams($scope.query);
      $scope.results = Search.query();
    }

    $scope.addToPlaylist = function(mediaId) {
      Playlist.add({id: mediaId}, function(added) {
        $scope.gotoPlaylist();
      });
    }

    $scope.prevPage = function() {
      if ($scope.query.page || $scope.query.page > 0) {
        $scope.query.page = Number($scope.query.page) - 1;
      } else {
        $scope.query.page = 1;
      }
      $route.updateParams($scope.query);
      $scope.results = Search.query($scope.query);
    }

    $scope.nextPage = function() {
      if ($scope.query.page) {
        $scope.query.page = Number($scope.query.page) + 1;
      } else {
        $scope.query.page = 2;
      }
      $route.updateParams($scope.query);
      $scope.results = Search.query($scope.query);
    }
  }
]);

discoDjControllers.controller("PlaylistCtrl", ["$scope", "$location", "$routeParams", "$interval", "Playlist",
  function($scope, $location, $routeParams, $interval, Playlist) {
    var inProgress = false;
    var lastRefresh = -1;

    var refreshPlaylist = function() {
      if (!inProgress && lastRefresh < new Date().getTime() - 1000) {
        inProgress = true;
        $scope.playlist = Playlist.query(function() {
          lastRefresh = new Date().getTime();
          inProgress = false;
        });
      }
    }
    refreshPlaylist();

    var stop = $interval(refreshPlaylist, 200);

    $scope.$on("$destroy", function() {
      $interval.cancel(stop);
    });

    $scope.gotoSearch = function() {
      $location.path("/search").search($routeParams);
    }

    $scope.moveUp = function(id) {
      Playlist.moveUp({id: id});

      for (var num = 1; num < $scope.playlist.playlist.length; num++) {
        var media = $scope.playlist.playlist[num];
        if (media.id === id) {
          var swapsy = $scope.playlist.playlist[num - 1];
          $scope.playlist.playlist[num - 1] = media;
          $scope.playlist.playlist[num] = swapsy;
          break;
        }
      }
    }

    $scope.moveDown = function(id) {
      Playlist.moveDown({id: id});

      for (var num = 0; num < $scope.playlist.playlist.length - 1; num++) {
        var media = $scope.playlist.playlist[num];
        if (media.id === id) {
          var swapsy = $scope.playlist.playlist[num + 1];
          $scope.playlist.playlist[num + 1] = media;
          $scope.playlist.playlist[num] = swapsy;
          break;
        }
      }
    }
  }
]);

discoDjControllers.controller("PlayerCtrl", ["$scope", "Playlist",
  function($scope, Playlist) {
    var videoPlayer = angular.element("video");
    var audioPlayer = angular.element("audio");

    var playVideo = function(videoId) {
      $scope.videoPlayerVisible = true;
      videoPlayer.attr("src", "./media?id=" + encodeURIComponent(videoId));
      videoPlayer.get(0).load();
      videoPlayer.get(0).play();
    }

    var playAudio = function(audioId) {
      $scope.audioPlayerVisible = true;
      audioPlayer.attr("src", "./media?id=" + encodeURIComponent(audioId));
      audioPlayer.get(0).load();
      audioPlayer.get(0).play();
    }

    var playNext = function() {
      $scope.videoPlayerVisible = false;
      $scope.audioPlayerVisible = false;
      $scope.loading = true;

      $scope.nowPlaying = Playlist.next(function(next) {
        if (!next.queueEmpty) {
          if (next.type == "Video") {
            playVideo(next.id);
          } else {
            playAudio(next.id);
          }
        }
        $scope.loading = false;
      });
    }

    videoPlayer.bind("ended", playNext);
    audioPlayer.bind("ended", playNext);

    // Init by loading next
    playNext();
  }
]);
