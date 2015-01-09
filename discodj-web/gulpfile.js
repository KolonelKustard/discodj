var gulp = require("gulp");
var browserify = require("browserify");
var transform = require("vinyl-transform");

gulp.task("browserify", function () {
  var browserified = transform(function(filename) {
    var b = browserify(filename);
    return b.bundle();
  });

  return gulp.src(["./src/main/webapp/js/app.js"])
    .pipe(browserified)
    .pipe(gulp.dest("./src/main/webapp/dist"));
});

gulp.task("watch", function() {
  gulp.watch("./src/main/webapp/js/**/*.js", ["browserify"]);
});

gulp.task("test", function() {
});

gulp.task("default", ["browserify"], function() {
});
