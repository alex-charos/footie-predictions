'use strict';

angular.module('footierepoApp')
    .controller('PropertiesDetailController', function ($scope, $rootScope, $stateParams, entity, Properties) {
        $scope.properties = entity;
        $scope.load = function (id) {
            Properties.get({id: id}, function(result) {
                $scope.properties = result;
            });
        };
        var unsubscribe = $rootScope.$on('footierepoApp:propertiesUpdate', function(event, result) {
            $scope.properties = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
