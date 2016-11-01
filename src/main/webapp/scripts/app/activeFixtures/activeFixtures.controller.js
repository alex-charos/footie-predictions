'use strict';

angular.module('footierepoApp')
    .controller('ActiveFixturesController', function ($scope, Principal, Fixture, Prediction, User) {


        $scope.fixtures = [];
        $scope.predictions = [];
        $scope.users = [];
        User.query(function(userData) {
            $scope.users =userData;
            Fixture.query(function(data) {
                $scope.fixtures = data;
                Prediction.query(function(predData) {
                    predData.sort(function(a,b) {
                        if(a.points != b.points){
                            return (a.points < b.points) ? 1 : -1;
                        }else if(a.correctScores != b.correctScores){
                            return (a.correctScores < b.correctScores) ? 1 : -1;
                        }else if(a.correctResults != b.correctResults){
                            return (a.correctResults < b.correctResults) ? 1 : -1;
                        }else{
                            if(a.username == "berg") return -11;
                            if(b.username == "berg") return 1;
                             if(a.username == "alexlfc") return -11;
                            if(b.username == "alexlfc") return 1;
                            return 1;
                        }
                    });
                    $scope.predictions = predData;
                    $scope.editedPredictionsActive = editData(data , predData);
                })

            });
        });

    });



function editData(fixtures , predictions){

    var customFixtures = [];
    var numberOfPredictions = predictions.length;


    for(var i=0;i<fixtures.length;i++){

        if(fixtures[i].group == 'A-4' || fixtures[i].group == 'B-4' || fixtures[i].group == 'C-4' || fixtures[i].group == 'D-4' || fixtures[i].group == 'E-4' || fixtures[i].group == 'F-4' || fixtures[i].group == 'H-4' || fixtures[i].group == 'G-4') {

            var customFixture = {};
            customFixture.home = fixtures[i].home;
            customFixture.away = fixtures[i].away;
            customFixture.homeFlag = fixtures[i].homeFlag;
            customFixture.awayFlag = fixtures[i].awayFlag;
            customFixture.result = '';
            if (fixtures[i].homeGoals != null && fixtures[i].awayGoals != null) {
                customFixture.result = fixtures[i].homeGoals + '-' + fixtures[i].awayGoals;
            }
            customFixture.group = fixtures[i].group;
            customFixture.order = fixtures[i].order;
            customFixture.preds = [];
            for (var j = 0; j < numberOfPredictions; j++) {
                var tmpPrediction = {};
                tmpPrediction.score = '';
                if (predictions[j].resultPerEvent[fixtures[i].id] && predictions[j].resultPerEvent[fixtures[i].id].homeScore != null && predictions[j].resultPerEvent[fixtures[i].id].awayScore != null) {
                    tmpPrediction.score = predictions[j].resultPerEvent[fixtures[i].id].homeScore + '-' + predictions[j].resultPerEvent[fixtures[i].id].awayScore;
                }
                tmpPrediction.color = 'black';
                if (customFixture.result != '' && predictions[j].resultPerEvent[fixtures[i].id]) {
                    tmpPrediction.color = 'red';
                    if (customFixture.result == tmpPrediction.score) {
                        tmpPrediction.color = 'green';
                    } else if (((predictions[j].resultPerEvent[fixtures[i].id].homeScore == predictions[j].resultPerEvent[fixtures[i].id].awayScore) && (fixtures[i].homeGoals == fixtures[i].awayGoals))
                        || ((predictions[j].resultPerEvent[fixtures[i].id].homeScore > predictions[j].resultPerEvent[fixtures[i].id].awayScore) && (fixtures[i].homeGoals > fixtures[i].awayGoals))
                        || ((predictions[j].resultPerEvent[fixtures[i].id].homeScore < predictions[j].resultPerEvent[fixtures[i].id].awayScore) && (fixtures[i].homeGoals < fixtures[i].awayGoals))) {
                        tmpPrediction.color = 'blue';
                    }
                }
                customFixture.preds.push(tmpPrediction);
            }

            customFixtures.push(customFixture);
        }
    }

    return customFixtures;

}

