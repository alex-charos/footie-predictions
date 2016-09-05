'use strict';

angular.module('footierepoApp')
    .controller('PointsController', function ($scope, Prediction) {



        $scope.predictions = [];
        $scope.users = []
        Prediction.query(function(data) {

            data.sort(function(a,b) {
                if(a.points != b.points){
                    return a.points < b.points;
                }else if(a.correctScores != b.correctScores){
                    return a.correctScores < b.correctScores;
                }else if(a.correctResults != b.correctResults){
                    return a.correctResults < b.correctResults;
                }else{
                    if(a.username == "berg") return -11;
                    if(b.username == "berg") return 1;
                    return 1;
                }
            });
            $scope.predictions = data;
        });

    });

