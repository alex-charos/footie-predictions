'use strict';

angular.module('footierepoApp').controller('PropertiesDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Properties',
        function($scope, $stateParams, $uibModalInstance, entity, Properties) {

        $scope.properties = entity;
        $scope.load = function(id) {
            Properties.get({id : id}, function(result) {
                $scope.properties = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('footierepoApp:propertiesUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.properties.id != null) {
                Properties.update($scope.properties, onSaveSuccess, onSaveError);
            } else {
                Properties.save($scope.properties, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
