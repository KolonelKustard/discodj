var discoDjApp = angular.module("discoDjApp", []);

discoDjApp.controller("SearchCtrl", function($scope, $http) {
  $http.get("resources/search").success(function(data) {
    $scope.results = data;
  });
});
