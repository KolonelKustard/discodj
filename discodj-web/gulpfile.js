var gulp = require("gulp");
var browserify = require("browserify");
var transform = require("vinyl-transform");
var ngHtml2Js = require("gulp-ng-html2js");
var streamqueue = require("streamqueue");
var concat = require("gulp-concat");
var exorcist = require("exorcist");
var uglify = require("gulp-uglify");
var del = require("del");
var less = require("gulp-less");
var minifyCss = require("gulp-minify-css");

gulp.task("scripts", function () {
  var browserified = transform(function(filename) {
    var b = browserify({entries: [filename], debug: false});
    return b.bundle();
  });

  var exorcistified = transform(function() {
    return exorcist("./src/main/webapp/dist/discodj.min.js.map");
  });

  var app = gulp.src(["./src/main/webapp/js/app.js"])
    .pipe(browserified);

  var partials = gulp.src("./src/main/webapp/partials/*.html")
    .pipe(ngHtml2Js({
      moduleName: "discoDjTemplates",
      prefix: "partials/"
    }));

  return streamqueue({ objectMode: true }, app, partials)
    .pipe(exorcistified)
    .pipe(concat("discodj.min.js"))
    .pipe(uglify())
    .pipe(gulp.dest("./src/main/webapp/dist"));
});

gulp.task("less", function() {
  return gulp.src(["./src/main/webapp/less/discodj.less"])
    .pipe(less())
    .pipe(minifyCss())
    .pipe(gulp.dest("./src/main/webapp/dist"));
});

gulp.task("watch", function() {
  gulp.watch("./src/main/webapp/js/**/*.js", ["scripts"]);
  gulp.watch("./src/main/webapp/less/**/*.less", ["less"]);
});

gulp.task("clean", function() {
  del(["./src/main/webapp/dist"]);
});

gulp.task("test", function() {
});

gulp.task("default", ["scripts", "less"], function() {
});
