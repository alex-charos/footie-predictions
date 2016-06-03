'use strict';

angular.module('footierepoApp')
	.controller('FixtureDeleteController', function($scope, $uibModalInstance, entity, Fixture) {

        $scope.fixture = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Fixture.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
