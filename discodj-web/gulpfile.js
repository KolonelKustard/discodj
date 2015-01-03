var gulp = require("gulp");
var usemin = require('gulp-usemin');
var uglify = require('gulp-uglify');
var minifyHtml = require('gulp-minify-html');
var minifyCss = require('gulp-minify-css');
var rev = require('gulp-rev');

gulp.task("default", function() {
  gulp.src('./src/main/webapp/*.html')
    .pipe(usemin({
      css: [minifyCss(), 'concat'],
      html: [minifyHtml({empty: true})],
      js: [uglify(), rev()]
    }))
    .pipe(gulp.dest('./src/main/webapp/build/'));
});

gulp.task("test", function() {
});
