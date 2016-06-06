'use strict';

angular.module('footierepoApp')
    .controller('MainController', function ($scope, Principal, Fixture, Prediction, User) {


    	$scope.fixtures = [];
    	$scope.predictions = [];
    	$scope.users = []
    	User.query(function(userData) {
    		$scope.users =userData;
			Fixture.query(function(data) {
                console.debug(data);
	    			$scope.fixtures = data;
	    			Prediction.query(function(predData) {
                        console.debug(predData);
	    				$scope.predictions = predData;


	    			})

	    	});
		});

    });
