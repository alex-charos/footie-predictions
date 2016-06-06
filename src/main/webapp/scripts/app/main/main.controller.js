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
                        $scope.editedPredictions = editData(data , predData);
	    			})

	    	});
		});

    });



function editData(fixtures , predictions){
    console.debug(fixtures);
    console.debug(predictions);
    var customFixtures = [];

    predictions.sort(function(a,b) {
        if(a.points != b.points){
            return a.points > b.points;
        }else if(a.correctScores != b.correctScores){
            return a.correctScores < b.correctScores;
        }else if(a.correctResults != b.correctResults){
            return a.correctResults < b.correctResults;
        }else{
            if(a.username == "berg") return 1;
            if(b.username == "berg") return -1;
            return -1;
        }
    });

    var numberOfPredictions = predictions.length;
    console.debug('numberOfPredictions');
    console.debug(numberOfPredictions);


    for(var i=0;i<fixtures.length;i++){
        var customFixture = {};
        customFixture.home = fixtures[i].home;
        customFixture.away = fixtures[i].away;
        customFixture.result = fixtures[i].result;
        customFixture.group = fixtures[i].group;
        customFixture.order = fixtures[i].order;
        customFixture.predictions = [];
        for(var j=0;j<numberOfPredictions;j++){
            customFixture.predictions.push(predictions[j].resultPerEvent[fixtures[i].id].homeScore+'-'+predictions[j].resultPerEvent[fixtures[i].id].awayScore);
        }
        customFixtures.push(customFixture);
    }

    return customFixtures;

}
