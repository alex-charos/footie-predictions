'use strict';

angular.module('footierepoApp')
	.controller('PropertiesDeleteController', function($scope, $uibModalInstance, entity, Properties) {

        $scope.properties = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Properties.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
