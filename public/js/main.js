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
        $http.get('204773031/streams/time').
            success(function(data) {
              console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };

    $scope.getLatLngStream = function() {
        $http.get('204773031/streams/latlng').
            success(function(data) {
              console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };

    $scope.getVelocityStream = function() {
        $http.get('204773031/streams/velocity').
            success(function(data) {
              console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };

    $scope.getMovingStream = function() {
        $http.get('204773031/streams/moving').
            success(function(data) {
              console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };

    $scope.getHeartrateStream = function() {
        $http.get('204773031/streams/heartrate').
            success(function(data) {
              console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };

    $scope.getWattsStream = function() {
        $http.get('204773031/streams/watts').
            success(function(data) {
              console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };

    $scope.getCadenceStream = function() {
        $http.get('204773031/streams/cadence').
            success(function(data) {
              console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };

    $scope.getAltitudeStream = function() {
        $http.get('204773031/streams/altitude').
            success(function(data) {
              console.log(data);
            }).
            error(function(e) {
                console.log("Error fetching all bets: " + e);
            })
    };
}]);
