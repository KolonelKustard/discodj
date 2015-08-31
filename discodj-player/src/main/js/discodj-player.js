var http = require("http");
var mediaPlayer = require("./mplayer.js");
var log4js = require("log4js");

var logger = log4js.getLogger("discodj-player");

module.exports.start = function(servicesBaseUrl) {
  logger.info("Starting up DiscoDJ Player with services url " + servicesBaseUrl);
  checkForNext(servicesBaseUrl);
};

var checkForNext = function(servicesBaseUrl) {
  var getNext = function() {
    logger.debug("Fetching next song to play");
    http.get(servicesBaseUrl + "/api/playlist/next", function(res) {
      var body = "";

      res.on("data", function(chunk) {
        body += chunk;
      });

      res.on("end", function() {
        processResponse(JSON.parse(body));
      });
    }).on("error", function(err) {
      logger.error("Failed to fetch next: " + err);
      waitAndTryAgain();
    });
  }

  var waitAndTryAgain = function() {
    logger.debug("Waiting before trying to fetch next");
    setTimeout(function() {
      getNext();
    }, 1000);
  };

  var processResponse = function(res) {
    if (res.queueEmpty) {
      logger.debug("Nothing to be played");
      waitAndTryAgain();
    } else {
      logger.debug("Playing next media: " + res);
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
