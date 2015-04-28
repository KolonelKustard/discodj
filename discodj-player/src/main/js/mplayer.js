var spawn = require('child_process').spawn;

module.exports.play = function(mediaUrl, finished) {
  var mplayer = spawn("mplayer", ["-fs", mediaUrl]);

  mplayer.stdout.on("data", function (data) {
    console.log('stdout: ' + data);
  });

  mplayer.stderr.on("data", function (data) {
    console.log('stderr: ' + data);
  });

  mplayer.on("close", function (code) {
    console.log('child process exited with code ' + code);
    finished();
  });
}
