'use strict';

angular.module('footierepoApp')
    .controller('PointsController', function ($scope, Prediction) {



        $scope.predictions = [];
        $scope.users = []
        Prediction.query(function(data) {

            data.sort(function(a,b) {
                if(a.points != b.points){
                    return (a.points < b.points) ? 1 : -1;
                }else if(a.correctScores != b.correctScores){
                    return (a.correctScores < b.correctScores) ? 1 : -1;
                }else if(a.correctResults != b.correctResults){
                    return (a.correctResults < b.correctResults) ? 1 : -1;
                }else{
                    if(a.username == "berg") return -11;
                    if(b.username == "berg") return 1;
                    if(a.username === "alexlfc") return -11;
                    if(b.username === "alexlfc") return 1;
                    return 1;
                }
            });
            $scope.predictions = data;
        });

    });

