'use strict';

angular.module('footierepoApp')
    .controller('PredictionDetailController', function ($scope, $rootScope, $stateParams, entity, Prediction) {
        $scope.prediction = entity;
        $scope.load = function (id) {
            Prediction.get({id: id}, function(result) {
                $scope.prediction = result;
            });
        };
        var unsubscribe = $rootScope.$on('footierepoApp:predictionUpdate', function(event, result) {
            $scope.prediction = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
