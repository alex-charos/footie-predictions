'use strict';

angular.module('footierepoApp').controller('FixtureDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fixture',
        function($scope, $stateParams, $uibModalInstance, entity, Fixture) {

        $scope.fixture = entity;
        $scope.load = function(id) {
            Fixture.get({id : id}, function(result) {
                $scope.fixture = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('footierepoApp:fixtureUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.fixture.id != null) {
                Fixture.update($scope.fixture, onSaveSuccess, onSaveError);
            } else {
                Fixture.save($scope.fixture, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
