var gulp = require('gulp');
var connect = require('gulp-connect')
var webpack = require('gulp-webpack')
const path = require('path')


gulp.task('task2', function () {
  console.log("In task2")
});

gulp.task('connect', function () {
  connect.server({
    port: 8000,
    livereload: true
  });
});

gulp.task('bundle', function () {
  gulp.src('app/js/main.js')
    .pipe(webpack({
        output: {
          filename: '[hash].js',
          path: path.resolve(__dirname, 'dist')
        },
      }, null, function(err, stats){
        console.log(stats)
        console.log(`${stats['hash']}.js`)
       }
     )
    )
    .pipe(gulp.dest('./build/'));
});

gulp.task('html', function () {
  gulp.src('./app/*.html')
    .pipe(connect.reload());
});

gulp.task('watch', function () {
  gulp.watch([
    './app/*.html',
    './app/**/*.js',
  ], ['html']);
})


gulp.task('default', ['connect', 'watch', 'bundle']);
