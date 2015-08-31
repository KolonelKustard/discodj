var spawn = require("child_process").spawn;
var log4js = require("log4js");

var logger = log4js.getLogger("mplayer");

module.exports.play = function(mediaUrl, finished) {
  var args = ["-fs", mediaUrl];
  logger.debug("Calling mplayer with args " + args);
  var mplayer = spawn("mplayer", args);

  mplayer.stdout.on("data", function (data) {
    logger.info(data);
  });

  mplayer.stderr.on("data", function (data) {
    logger.error(data);
  });

  mplayer.on("close", function (code) {
    logger.debug("mplayer exited with code " + code);
    finished();
  });
}
