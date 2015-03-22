var gulp = require("gulp");
var minimist = require("minimist");
var browserify = require("browserify");
var aliasify = require("aliasify");
var transform = require("vinyl-transform");
var ngHtml2Js = require("gulp-ng-html2js");
var streamqueue = require("streamqueue");
var concat = require("gulp-concat");
var exorcist = require("exorcist");
var uglify = require("gulp-uglify");
var del = require("del");
var less = require("gulp-less");
var minifyCss = require("gulp-minify-css");
var webserver = require("gulp-webserver");

var options = minimist(process.argv.slice(2), {
  "string": ["target"],
  "boolean": ["stubbed"],
  "default": {
    "target": "./src/main/webapp/dist",
    "stubbed": false
  }
});

gulp.task("scripts", function () {
  if (options.stubbed) {
    return processScripts({
      "./services.js": "./src/main/webapp/stub/services-stubs.js"
    });
  } else {
    return processScripts();
  }
});

gulp.task("less", function() {
  return gulp.src(["./src/main/webapp/less/discodj.less"])
    .pipe(less())
    .pipe(minifyCss())
    .pipe(gulp.dest(options.target));
});

gulp.task("watch", function() {
  gulp.watch(["./src/main/webapp/js/**/*.js", "./src/main/webapp/stub/**/*.js"], ["scripts"]);
  gulp.watch("./src/main/webapp/less/**/*.less", ["less"]);
});

gulp.task("webserver", ["watch"], function() {
  gulp.src("./src/main/webapp")
    .pipe(webserver({
      livereload: true,
      open: true,
      proxies: [{
        source: "/discodj/resources",
        target: "http://localhost:58008/discodj/resources"
      }]
    }));
});

gulp.task("clean", function() {
  del([options.target]);
});

gulp.task("test", function() {
});

gulp.task("default", ["scripts", "less"], function() {
});

var processScripts = function(aliases) {
  var b = browserify({debug: false});
  if (aliases) {
    var a = aliasify.configure({
      aliases: aliases,
      configDir: __dirname,
      verbose: true
    });
    b.transform(a);
  }
  var browserified = transform(function(filename) {
    b.add(filename);
    return b.bundle();
  });

  var exorcistified = transform(function() {
    return exorcist(options.target + "/discodj.min.js.map");
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
    .pipe(gulp.dest(options.target));
}
