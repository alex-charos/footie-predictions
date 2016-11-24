'use strict';

angular.module('footierepoApp').controller('FixtureDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fixture',
        function($scope, $stateParams, $uibModalInstance, entity, Fixture) {

        entity.kickOff = Date.parse(entity.kickOff);
        $scope.fixture = entity;
        $scope.load = function(id) {
            Fixture.get({id : id}, function(result) {
                
                result.kickOff = Date.parse(result.kickOff);
                $scope.fixture = result;
            });
        };
        $scope.$watch('fixture.kickOff', function(newval, oldval) {
            if (! (newval instanceof Date)) {
                $scope.fixture.kickOff = new Date(Date.parse(newval));
            }
            
            
        })

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
            $scope.fixture.kickOff = $scope.fixture.kickOff.getTime()/1000;
            if ($scope.fixture.id != null) {
                Fixture.update($scope.fixture, onSaveSuccess, onSaveError);
            } else {
                Fixture.save($scope.fixture, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
         $scope.datePickerForKickOff = {};

        $scope.datePickerForKickOff.status = {
            opened: false
        };


            $scope.datePickerForKickOffOpen = function($event) {
            $scope.datePickerForKickOff.status.opened = true;
        };
}]);
