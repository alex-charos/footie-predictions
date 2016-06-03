'use strict';

angular.module('footierepoApp')
	.controller('PredictionDeleteController', function($scope, $uibModalInstance, entity, Prediction) {

        $scope.prediction = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Prediction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
