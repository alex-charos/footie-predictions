'use strict';

angular.module('footierepoApp')
    .controller('MainController', function ($scope, Principal, Fixture, Prediction, User) {


    	$scope.fixtures = [];
    	$scope.predictions = [];
    	$scope.users = []
    	User.query(function(userData) {
    		$scope.users =userData;
			Fixture.query(function(data) {
	    			$scope.fixtures = data;
	    			Prediction.query(function(predData) {
	    				$scope.predictions = predData;

	    			})

	    	});
		});


    	$scope.getAvatarByUsername = function(username) {
    		for (var i = 0; i < $scope.users.length; i++) {
    			if ($scope.users[i].username === username) {
    				return $scope.users[i].avatar;
    			}
    		}

    		return "";
    	}


       

    });
