'use strict';

angular.module('footierepoApp').controller('PredictionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Prediction',
        function($scope, $stateParams, $uibModalInstance, entity, Prediction) {

        $scope.prediction = entity;
        $scope.load = function(id) {
            Prediction.get({id : id}, function(result) {
                $scope.prediction = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('footierepoApp:predictionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.prediction.id != null) {
                Prediction.update($scope.prediction, onSaveSuccess, onSaveError);
            } else {
                Prediction.save($scope.prediction, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
