var main = angular.module("webPortal", []);
main.controller('trifectaController', ['$scope', '$http', '$timeout', function($scope, $http, $timeout) {
    var accessToken = "";

    $scope.getAthlete = function() {
        $http.get('/athlete/').
            success(function(data) {
            	console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };

    $scope.listKOMS = function() {
        $http.get('/koms/').
            success(function(data) {
            	console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };

    $scope.listFriends = function() {
        $http.get('/friends/').
            success(function(data) {
              console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };

    $scope.getTimeStream = function() {
        $http.get('/streams/time').
            success(function(data) {
              console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };
}]);
