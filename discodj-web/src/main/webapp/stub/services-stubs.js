var mediaStubs = require("./media-stubs.js");
var mediaStubUtils = require("./media-stub-utils.js");
var playlist = require("./playlist-stub.js");

require("angular");

var discoDjServices = angular.module("discoDjServices", []);

discoDjServices.factory("Search", [
  function() {
    var query = function(params) {
      return mediaStubUtils.search(mediaStubs, params, playlist.query().playlist);
    }

    return {
      query: query
    };
  }
]);

discoDjServices.factory("Playlist", [
  function() {
    return {
      query: function(cb) {
        var play = playlist.query();
        if (cb) {
          cb(play);
        }
        return play;
      },
      add: function(media, cb) {
        var ok = playlist.add(media.id, mediaStubs);
        if (cb) {
          cb(ok);
        }
        return ok;
      },
      moveUp: function(media, cb) {
        var ok = playlist.moveUp(media.id, mediaStubs);
        if (cb) {
          cb(ok);
        }
        return ok;
      },
      moveDown: function(media, cb) {
        var ok = playlist.moveDown(media.id, mediaStubs);
        if (cb) {
          cb(ok);
        }
        return ok;
      },
      next: function(cb) {
        var next = playlist.next();
        if (cb) {
          cb(next);
        }
        return next;
      }
    };
  }
]);