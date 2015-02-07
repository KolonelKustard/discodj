var mediaStubs = require("./media-stubs.js");
var mediaStubUtils = require("./media-stub-utils.js");

require("angular");
require("angular-resource");

var discoDjServices = angular.module("discoDjServices", ["ngResource"]);

discoDjServices.factory("Search", ["$resource",
  function($resource) {
    var query = function(params) {
      return mediaStubUtils.search(mediaStubs, params);
    }

    return {
      query: query
    };
  }
]);

discoDjServices.factory("Playlist", [ "$resource",
  function($resource) {
    return $resource("resources/playlist", {}, {
      query : {
        method : "GET"
      },
      add : {
        method : "GET",
        url : "resources/playlist/add"
      },
      moveUp : {
        method : "GET",
        url : "resources/playlist/moveUp"
      },
      moveDown : {
        method : "GET",
        url : "resources/playlist/moveDown"
      },
      next : {
        method : "GET",
        url : "resources/playlist/next"
      }
    });
  }
]);