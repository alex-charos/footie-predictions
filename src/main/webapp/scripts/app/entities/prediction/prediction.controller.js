'use strict';

angular.module('footierepoApp')
    .controller('PredictionController', function ($scope, $state, Prediction) {

        $scope.predictions = [];
        $scope.loadAll = function() {
            Prediction.query(function(result) {
               $scope.predictions = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.prediction = {
                username: null,
                points: null,
                correctScores: null,
                correctResults: null,
                id: null
            };
        };
    });
