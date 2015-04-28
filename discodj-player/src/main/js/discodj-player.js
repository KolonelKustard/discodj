var http = require("http");
var mediaPlayer = require("./mplayer.js");

module.exports.start = function(servicesBaseUrl) {
  checkForNext(servicesBaseUrl);
};

var checkForNext = function(servicesBaseUrl) {
  var getNext = function() {
    http.get(servicesBaseUrl + "/api/playlist/next", function(res) {
      var body = "";

      res.on("data", function(chunk) {
        body += chunk;
      });

      res.on("end", function() {
        processResponse(JSON.parse(body));
      });
    }).on("error", function(err) {
      console.log("Failed to reach services: " + err);
      waitAndTryAgain();
    });
  }

  var waitAndTryAgain = function() {
    setTimeout(function() {
      getNext();
    }, 1000);
  };

  var processResponse = function(res) {
    if (res.queueEmpty) {
      waitAndTryAgain();
    } else {
      playMedia(res)
    }
  };

  var playMedia = function (media) {
    mediaPlayer.play(servicesBaseUrl + "/media?id="
        + encodeURIComponent(media.id), function() {
      getNext();
    });
  };

  getNext();
};
