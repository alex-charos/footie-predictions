'use strict';

angular.module('footierepoApp')
    .controller('MainController', function ($scope, Principal, Fixture, Prediction) {


    	$scope.fixtures = [];
    	$scope.predictions = [];
    	Fixture.query(function(data) {
    			$scope.fixtures = data;
    			Prediction.query(function(predData) {
    				$scope.predictions = predData;
    				
    			})

    	});




       

    });
